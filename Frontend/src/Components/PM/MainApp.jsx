import React, { useState } from 'react';
import { Container, Button, Modal } from 'react-bootstrap';
import SearchBar from './SearchBar';
import ProjectForm from './ProjectForm';
import BeneficiaryForm from './BeneficiaryForm';
import BeneficiaryTable from './BeneficiaryTable';

const MainApp = () => {

  
  const [projects, setProjects] = useState([]);
  const [beneficiaries, setBeneficiaries] = useState([]);

  const [showProjectModal, setShowProjectModal] = useState(false);
  const [showBeneficiaryModal, setShowBeneficiaryModal] = useState(false);

  const addProject = (project) => {
    setProjects((prev) => [...prev, project]);
    console.log(projects);
    setShowProjectModal(false);
  };

  const addBeneficiary = (beneficiary) => {
    setBeneficiaries((prev) => [...prev, beneficiary]);
    setShowBeneficiaryModal(false);
  };

  const handleSearch = (criteria) => {
    // Implement search logic here
  };

  return (
    <Container>
      <SearchBar onSearch={handleSearch} />
      <Button variant="primary" onClick={() => setShowProjectModal(true)}>
        Add Project
      </Button>
      <Button variant="primary" onClick={() => setShowBeneficiaryModal(true)}>
        Add Beneficiary
      </Button>

      <Modal show={showProjectModal} onHide={() => setShowProjectModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Add Project</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <ProjectForm addProject={addProject} />
        </Modal.Body>
      </Modal>

      <Modal show={showBeneficiaryModal} onHide={() => setShowBeneficiaryModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Add Beneficiary</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <BeneficiaryForm projects={projects} addBeneficiary={addBeneficiary} />
        </Modal.Body>
      </Modal>

      <pre>{JSON.stringify({ projects, beneficiaries }, null, 2)}</pre>
      <BeneficiaryTable beneficiaries={beneficiaries} setBeneficiaries={setBeneficiaries}/>
    </Container>
  );
};

export default MainApp;
