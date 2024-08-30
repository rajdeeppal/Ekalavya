import React, { useState } from 'react';
import { Table, Button, Collapse, Form } from 'react-bootstrap';
import Accordion from 'react-bootstrap/Accordion';

const BeneficiaryTable = ({ beneficiaries, setBeneficiaries }) => {
  const [editMode, setEditMode] = useState({});
  const [open, setOpen] = useState({});
  const [editActivityMode, setEditActivityMode] = useState({});
  const [newActivity, setNewActivity] = useState({});
  const [newComponent, setNewComponent] = useState('');
  const availableComponents = [
    'Raw Metarial',
    'Inbox'
  ];
  const [showAddComponent, setShowAddComponent] = useState(false);

  const toggleCollapse = (index) => {
    setOpen(prevState => ({ ...prevState, [index]: !prevState[index] }));
  };

  const handleEditClick = (index) => {
    setEditMode(prevState => ({ ...prevState, [index]: !prevState[index] }));
  };

  const handleInputChange = (index, e) => {
    const { name, value } = e.target;
    const updatedBeneficiaries = [...beneficiaries];
    updatedBeneficiaries[index][name] = value;
    setBeneficiaries(updatedBeneficiaries);
  };

  const handleActivityInputChange = (beneficiaryIndex, componentIndex, activityIndex, e) => {
    const { name, value } = e.target;
    const updatedBeneficiaries = [...beneficiaries];
    updatedBeneficiaries[beneficiaryIndex].components[componentIndex].activities[activityIndex][name] = value;
    setBeneficiaries(updatedBeneficiaries);
  };

  const handleEditActivityClick = (beneficiaryIndex, componentIndex, activityIndex) => {
    setEditActivityMode(prevState => ({
      ...prevState,
      [`${beneficiaryIndex}-${componentIndex}-${activityIndex}`]: !prevState[`${beneficiaryIndex}-${componentIndex}-${activityIndex}`],
    }));
  };

  const handleSaveActivity = (beneficiaryIndex, componentIndex, activityIndex) => {
    setEditActivityMode(prevState => ({
      ...prevState,
      [`${beneficiaryIndex}-${componentIndex}-${activityIndex}`]: false,
    }));

    // const beneficiary = beneficiaries[beneficiaryIndex];
    // const id = beneficiary.id;
    // const activity = beneficiary.components[componentIndex].activities[activityIndex];

    // try {
    //   const response = await axios.post(`http://localhost:8000/updated/${id}`, {
    //     componentIndex,
    //     activityIndex,
    //     activity
    //   });
    //   console.log('Success:', response.data);
    // } catch (error) {
    //   console.error('Error:', error);
    // }
  };

  const handleSave = (index) => {
    setEditMode(prevState => ({ ...prevState, [index]: false }));
    // Optionally handle save to backend here
    // try {
    //   const response = await axios.post(`http://localhost:8000/updated/${id}`, beneficiary);
    //   console.log('Success:', response.data);
    // } catch (error) {
    //   console.error('Error:', error);
    // }
  };

  const handleAddActivity = (beneficiaryIndex, componentIndex) => {
    const updatedBeneficiaries = [...beneficiaries];
    const newActivityObj = {
      nameOfWork: '',
      typeOfUnit: '',
      unitRate: '',
      noOfUnits: '',
      totalCost: '',
      beneficiaryContribution: '',
      grantAmount: '',
      yearOfSanction: '',
    };
    updatedBeneficiaries[beneficiaryIndex].components[componentIndex].activities.push(newActivityObj);
    setBeneficiaries(updatedBeneficiaries);
  };

  const handleAddComponent = (beneficiaryIndex) => {
    if (!newComponent) return;

    const updatedBeneficiaries = [...beneficiaries];
    const selectedComponent = {
      name: newComponent,
      activities: []
    };
    updatedBeneficiaries[beneficiaryIndex].components.push(selectedComponent);
    setBeneficiaries(updatedBeneficiaries);

    // try {
    //   const beneficiary = beneficiaries[beneficiaryIndex];
    //   const response = await axios.post(`http://localhost:8000/api/beneficiaries/${beneficiary.id}/components`, selectedComponent);
    //   console.log('Component saved:', response.data);
    // } catch (error) {
    //   console.error('Error saving component:', error);
    // }

    setNewComponent(''); // Clear the selected component after adding
    setShowAddComponent(false);
  };

  return (
    <div>
      <h3>Beneficiary List</h3>
      <Table bordered hover responsive>
        <thead>
          <tr>
            <th>Project Name</th>
            <th>Beneficiary Name</th>
            <th>Father/Husband Name</th>
            <th>Village</th>
            <th>Mandal</th>
            <th>District</th>
            <th>State</th>
            <th>Aadhar</th>
            <th>Survey No.</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {beneficiaries.map((beneficiary, index) => (
            <React.Fragment key={beneficiary.id}>
              <tr>
                {editMode[index] ? (
                  <>
                    <td>
                      <Form.Control
                        type="text"
                        name="beneficiaryName"
                        value={beneficiary.beneficiaryName}
                        onChange={(e) => handleInputChange(index, e)}
                      />
                    </td>
                    <td>
                      <Form.Control
                        type="text"
                        name="fatherHusbandName"
                        value={beneficiary.fatherHusbandName}
                        onChange={(e) => handleInputChange(index, e)}
                      />
                    </td>
                    <td>
                      <Form.Control
                        type="text"
                        name="village"
                        value={beneficiary.village}
                        onChange={(e) => handleInputChange(index, e)}
                      />
                    </td>
                    <td>
                      <Form.Control
                        type="text"
                        name="mandal"
                        value={beneficiary.mandal}
                        onChange={(e) => handleInputChange(index, e)}
                      />
                    </td>
                    <td>
                      <Form.Control
                        type="text"
                        name="district"
                        value={beneficiary.district}
                        onChange={(e) => handleInputChange(index, e)}
                      />
                    </td>
                    <td>
                      <Form.Control
                        type="text"
                        name="state"
                        value={beneficiary.state}
                        onChange={(e) => handleInputChange(index, e)}
                      />
                    </td>
                    <td>
                      <Form.Control
                        type="text"
                        name="aadhar"
                        value={beneficiary.aadhar}
                        onChange={(e) => handleInputChange(index, e)}
                      />
                    </td>
                    <td>
                      <Form.Control
                        type="text"
                        name="surveyNo"
                        value={beneficiary.surveyNo}
                        onChange={(e) => handleInputChange(index, e)}
                      />
                    </td>
                    <td>
                      <Button variant="success" onClick={() => handleSave(index)}>
                        Save
                      </Button>
                    </td>
                  </>
                ) : (
                  <>
                    <td>{beneficiary.projectName}</td>
                    <td>{beneficiary.beneficiaryName}</td>
                    <td>{beneficiary.fatherHusbandName}</td>
                    <td>{beneficiary.village}</td>
                    <td>{beneficiary.mandal}</td>
                    <td>{beneficiary.district}</td>
                    <td>{beneficiary.state}</td>
                    <td>{beneficiary.aadhar}</td>
                    <td>{beneficiary.surveyNo}</td>
                    <td>
                      <Button
                        variant="primary"
                        onClick={() => toggleCollapse(index)}
                        aria-controls={`collapse-${index}`}
                        aria-expanded={open[index]}
                      >
                        View Components
                      </Button>
                      <Button variant="warning" onClick={() => handleEditClick(index)}>
                        Edit
                      </Button>
                      
                    </td>
                  </>
                )}
              </tr>
              <tr>
                <td colSpan="10" style={{ padding: 0 }}>
                  <Collapse in={open[index]}>
                    <div id={`collapse-${index}`} style={{ padding: '10px' }}>
                    <Button onClick={() => setShowAddComponent(true)}>Add Component</Button>
                      {showAddComponent &&
                        <div>
                          <Form.Control
                            as="select"
                            value={newComponent}
                            onChange={(e) => setNewComponent(e.target.value)}
                          >
                            <option value="">Select Component</option>
                            {availableComponents.map((component, i) => (
                              <option key={i} value={component}>
                                {component}
                              </option>
                            ))}
                          </Form.Control>
                          <Button onClick={() => handleAddComponent(index)}>
                            Add
                          </Button>
                        </div>
                        }
                      {beneficiary.components.map((component, compIndex) => (
                        <div key={compIndex}>
                          <Accordion defaultActiveKey="0">
                            <Accordion.Item eventKey={compIndex}>
                              <Accordion.Header>{component.name}</Accordion.Header>
                              <Accordion.Body>
                                <Button
                                  variant="primary"
                                  onClick={() => handleAddActivity(index, compIndex)}
                                  style={{ marginBottom: '10px' }}
                                >
                                  Add Activity
                                </Button>
                                <Table striped bordered hover>
                                  <thead>
                                    <tr>
                                      <th>Name of the Work</th>
                                      <th>Type of Unit</th>
                                      <th>Unit Rate</th>
                                      <th>No. of Units</th>
                                      <th>Total Cost</th>
                                      <th>Beneficiary Contribution</th>
                                      <th>Grant Amount</th>
                                      <th>Year of Sanction</th>
                                      <th>Actions</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    {component.activities.map((activity, activityIndex) => (
                                      <tr key={activityIndex}>
                                        {editActivityMode[`${index}-${compIndex}-${activityIndex}`] ? (
                                          <>
                                            <td>
                                              <Form.Control
                                                type="text"
                                                name="nameOfWork"
                                                value={activity.nameOfWork}
                                                onChange={(e) => handleActivityInputChange(index, compIndex, activityIndex, e)}
                                              />
                                            </td>
                                            <td>
                                              <Form.Control
                                                type="text"
                                                name="typeOfUnit"
                                                value={activity.typeOfUnit}
                                                onChange={(e) => handleActivityInputChange(index, compIndex, activityIndex, e)}
                                              />
                                            </td>
                                            <td>
                                              <Form.Control
                                                type="text"
                                                name="unitRate"
                                                value={activity.unitRate}
                                                onChange={(e) => handleActivityInputChange(index, compIndex, activityIndex, e)}
                                              />
                                            </td>
                                            <td>
                                              <Form.Control
                                                type="text"
                                                name="noOfUnits"
                                                value={activity.noOfUnits}
                                                onChange={(e) => handleActivityInputChange(index, compIndex, activityIndex, e)}
                                              />
                                            </td>
                                            <td>
                                              <Form.Control
                                                type="text"
                                                name="totalCost"
                                                value={activity.totalCost}
                                                onChange={(e) => handleActivityInputChange(index, compIndex, activityIndex, e)}
                                              />
                                            </td>
                                            <td>
                                              <Form.Control
                                                type="text"
                                                name="beneficiaryContribution"
                                                value={activity.beneficiaryContribution}
                                                onChange={(e) => handleActivityInputChange(index, compIndex, activityIndex, e)}
                                              />
                                            </td>
                                            <td>
                                              <Form.Control
                                                type="text"
                                                name="grantAmount"
                                                value={activity.grantAmount}
                                                onChange={(e) => handleActivityInputChange(index, compIndex, activityIndex, e)}
                                              />
                                            </td>
                                            <td>
                                              <Form.Control
                                                type="text"
                                                name="yearOfSanction"
                                                value={activity.yearOfSanction}
                                                onChange={(e) => handleActivityInputChange(index, compIndex, activityIndex, e)}
                                              />
                                            </td>
                                            <td>
                                              <Button variant="success" onClick={() => handleSaveActivity(index, compIndex, activityIndex)}>
                                                Save
                                              </Button>
                                            </td>
                                          </>
                                        ) : (
                                          <>
                                            <td>{activity.nameOfWork}</td>
                                            <td>{activity.typeOfUnit}</td>
                                            <td>{activity.unitRate}</td>
                                            <td>{activity.noOfUnits}</td>
                                            <td>{activity.totalCost}</td>
                                            <td>{activity.beneficiaryContribution}</td>
                                            <td>{activity.grantAmount}</td>
                                            <td>{activity.yearOfSanction}</td>
                                            <td>
                                              <Button
                                                variant="warning"
                                                onClick={() => handleEditActivityClick(index, compIndex, activityIndex)}
                                              >
                                                Edit
                                              </Button>
                                            </td>
                                          </>
                                        )}
                                      </tr>
                                    ))}
                                  </tbody>
                                </Table>
                              </Accordion.Body>
                            </Accordion.Item>
                          </Accordion>
                        </div>
                      ))}
                    </div>
                  </Collapse>
                </td>
              </tr>
            </React.Fragment>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default BeneficiaryTable;
