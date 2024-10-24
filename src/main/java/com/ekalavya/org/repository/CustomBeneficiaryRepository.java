package com.ekalavya.org.repository;

import com.ekalavya.org.entity.M_Beneficiary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CustomBeneficiaryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Value("${base.url}")
    private String baseUrl;


    public List<M_Beneficiary> findBeneficiariesBasedOnCriteria(Map<String, String> params) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<M_Beneficiary> query = cb.createQuery(M_Beneficiary.class);
        List<Predicate> predicates = new ArrayList<>();
        Root<M_Beneficiary> beneficiaryRoot = query.from(M_Beneficiary.class);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            predicates.add(cb.equal(beneficiaryRoot.get(entry.getKey()), entry.getValue()));
        }
        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        return entityManager.createQuery(query).getResultList();
    }
    public Map<String, Object> findTaskUpdates(String employeeRole, String stage, Long employeeId, String projectName, String componentName, String stateName, String districtName) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.id AS project_id, b.id AS beneficiary_id, b.beneficiary_name, b.guardian_name, b.state_name, " +
                        "b.village_name, b.mandal_name, b.district_name, b.aadhar_number, c.id AS component_id, c.component_name, " +
                        "a.id AS activity_id, a.activity_name, t.id AS task_id, t.task_name, t.type_of_unit, t.units, t.rate_per_unit, " +
                        "t.total_cost, t.beneficiary_contribution, t.grant_amount, t.year_of_sanction, t.unit_remain, t.balance_remaining, " +
                        "t.beneficiary_contribution_remain, t.is_completed AS task_is_completed, t.is_sanction AS task_sanction, " +
                        "tu.id AS task_update_id, tu.created_date, tu.pending_with, tu.payee_name, tu.achievement_unit, tu.is_completed, " +
                        "tu.is_rejection_occurred, tu.account_number, tu.remarks, pd.id AS passbook_doc_id, pd.file_name AS passbook_file_name, " +
                        "od.id AS other_doc_id, od.file_name AS other_doc_file_name " +
                        "FROM project p " +
                        "JOIN m_beneficiary b ON b.project_id = p.id " +
                        "JOIN m_component c ON c.beneficiary_id = b.id " +
                        "JOIN m_activity a ON a.component_id = c.id " +
                        "JOIN m_task t ON t.activity_id = a.id " +
                        "LEFT JOIN m_task_update tu ON tu.task_id = t.id " +
                        "LEFT JOIN document pd ON pd.id = tu.passbook_doc_id " +
                        "LEFT JOIN document od ON od.task_update_id = tu.id " +
                        "WHERE p.project_name = :projectName "
        );

        // Use Optional to handle null values
        Optional.ofNullable(componentName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> sql.append("AND c.component_name = :componentName "));

        Optional.ofNullable(stateName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> sql.append("AND b.state_name = :stateName "));

        Optional.ofNullable(districtName)
                .filter(name -> !name.isEmpty())
                .ifPresent(name -> sql.append("AND b.district_name = :districtName "));

        // Add dynamic conditions based on employeeRole and stage
        if ("PM".equals(employeeRole)) {
            switch (stage) {
                case "sanction":
                    sql.append(" AND t.is_sanction = false");
                    break;
                case "inprogress":
                    sql.append(" AND tu.pending_with = 'PM' AND tu.is_rejection_occurred = false");
                    break;
                case "preview":
                    sql.append(" AND tu.pending_with = 'DE' AND tu.is_rejection_occurred = false");
                    break;
                case "rejection":
                    sql.append(" AND tu.pending_with = 'PM' AND tu.is_rejection_occurred = true");
                    break;
                default:
                    throw new RuntimeException("Unsupported stage for PM role: " + stage);
            }
        } else if ("DE".equals(employeeRole)) {
            sql.append(" AND tu.pending_with = 'DE' AND tu.domain_expert_emp_id = :employeeId");
        } else {
            throw new RuntimeException("Unsupported role: " + employeeRole);
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("projectName", projectName);

        Optional.ofNullable(componentName).ifPresent(name -> query.setParameter("componentName", name));
        Optional.ofNullable(stateName).ifPresent(name -> query.setParameter("stateName", name));
        Optional.ofNullable(districtName).ifPresent(name -> query.setParameter("districtName", name));

        if ("DE".equals(employeeRole)) {
            query.setParameter("employeeId", employeeId);
        }

        List<Object[]> results = query.getResultList();

        // Build the nested structure: Project → Beneficiaries → Components → Activities → Tasks → Task Updates
        Map<String, Object> projectData = new HashMap<>();
        if (!results.isEmpty()) {
            Object[] firstRow = results.get(0);
            Long projectId = (Long) firstRow[0];  // Assuming project_id is at index 0

            // Dynamically set the project ID and project name
            projectData.put("id", projectId);
            projectData.put("projectName", projectName);
        }

        List<Map<String, Object>> beneficiaries = new ArrayList<>();

        Map<Long, Map<String, Object>> beneficiaryMap = new HashMap<>();
        Map<Long, Map<String, Object>> componentMap = new HashMap<>();
        Map<Long, Map<String, Object>> activityMap = new HashMap<>();
        Map<Long, Map<String, Object>> taskMap = new HashMap<>();

        for (Object[] row : results) {
            Long beneficiaryId = (Long) row[1];
            Map<String, Object> beneficiary = beneficiaryMap.computeIfAbsent(beneficiaryId, id -> {
                Map<String, Object> b = new HashMap<>();
                b.put("id", id);
                b.put("beneficiaryName", row[2]);
                b.put("guardianName", row[3]);
                b.put("stateName", row[4]);
                b.put("villageName", row[5]);
                b.put("mandalName", row[6]);
                b.put("districtName", row[7]);
                b.put("aadharNumber", row[8]);
                b.put("components", new ArrayList<>());
                return b;
            });

            Long componentId = (Long) row[9];
            Map<String, Object> component = componentMap.computeIfAbsent(componentId, id -> {
                Map<String, Object> c = new HashMap<>();
                c.put("id", id);
                c.put("componentName", row[10]);
                c.put("activities", new ArrayList<>());
                return c;
            });

            Long activityId = (Long) row[11];
            Map<String, Object> activity = activityMap.computeIfAbsent(activityId, id -> {
                Map<String, Object> a = new HashMap<>();
                a.put("id", id);
                a.put("activityName", row[12]);
                a.put("tasks", new ArrayList<>());
                return a;
            });

            Long taskId = (Long) row[13];
            Map<String, Object> task = taskMap.computeIfAbsent(taskId, id -> {
                Map<String, Object> t = new HashMap<>();
                t.put("id", id);
                t.put("taskName", row[14]);
                t.put("typeOfUnit", row[15]);
                t.put("units", row[16]);
                t.put("ratePerUnit", row[17]);
                t.put("totalCost", row[18]);
                t.put("beneficiaryContribution", row[19]);
                t.put("grantAmount", row[20]);
                t.put("yearOfSanction", row[21]);
                t.put("unitRemain", row[22]);
                t.put("balanceRemaining", row[23]);
                t.put("beneficiaryContributionRemain", row[24]);
                t.put("isCompleted", row[25]);
                t.put("isSanction", row[26]);
                t.put("taskUpdates", new ArrayList<>()); // Initialize taskUpdates list
                return t;
            });

            // Add Task Update
            if (row[27] != null) {
                Map<String, Object> taskUpdate = new HashMap<>();
                taskUpdate.put("id", row[27]);
                taskUpdate.put("createdDate", row[28]);
                taskUpdate.put("pendingWith", row[29]);
                taskUpdate.put("payeeName", row[30]);
                taskUpdate.put("achievementUnit", row[31]);
                taskUpdate.put("isCompleted", row[32]);
                taskUpdate.put("isRejectionOccurred", row[33]);
                taskUpdate.put("accountNumber", row[34]);
                taskUpdate.put("remarks", row[35]);

                // Add passbookDoc file details
                if (row[36] != null) {
                    Map<String, Object> passbookDoc = new HashMap<>();
                    passbookDoc.put("id", row[36]);
                    passbookDoc.put("fileName", row[37]);
                    passbookDoc.put("downloadUrl", baseUrl + "/download-document/" + row[36]);  // URL to download file
                    taskUpdate.put("passbookDoc", passbookDoc);
                }

                // Add otherDocs details
                List<Map<String, Object>> otherDocuments = new ArrayList<>();
                if (row[38] != null) {
                    Map<String, Object> otherDoc = new HashMap<>();
                    otherDoc.put("id", row[38]);
                    otherDoc.put("fileName", row[39]);
                    otherDoc.put("downloadUrl", baseUrl + "/download-document/" + row[38]);  // URL to download each file
                    otherDocuments.add(otherDoc);
                }
                taskUpdate.put("otherDocs", otherDocuments);

                // Add task update to task
                ((List<Map<String, Object>>) task.get("taskUpdates")).add(taskUpdate);
            }

            // Add task to activity
            ((List<Map<String, Object>>) activity.get("tasks")).add(task);

            // Add activity to component
            ((List<Map<String, Object>>) component.get("activities")).add(activity);

            // Add component to beneficiary
            List<Map<String, Object>> components = (List<Map<String, Object>>) beneficiary.get("components");
            if (!components.contains(component)) {
                components.add(component);
            }

            // Add beneficiary to project
            if (!beneficiaries.contains(beneficiary)) {
                beneficiaries.add(beneficiary);
            }
        }

        projectData.put("beneficiaries", beneficiaries);
        return projectData;
    }
    }
