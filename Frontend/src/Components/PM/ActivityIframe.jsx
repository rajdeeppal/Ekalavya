import React, { useState } from 'react';
import { Form, Button } from 'react-bootstrap';

function ActivityIframe({ taskName, onSave, typeOfUnit, unitRate }) {
  const [activity, setActivity] = useState({
    task: taskName,
    nameOfWork: '',
    typeOfUnit: typeOfUnit,
    unitRate: unitRate,
    noOfUnits: '',
    totalCost: '',
    beneficiaryContribution: '',
    grantAmount: '',
    yearOfSanction: '',
  });

  const handleSave = () => {
    onSave(activity);
    setActivity({
      task: '',
      nameOfWork: '',
      typeOfUnit: '',
      unitRate: '',
      noOfUnits: '',
      totalCost: '',
      beneficiaryContribution: '',
      grantAmount: '',
      yearOfSanction: '',
    });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setActivity({ ...activity, [name]: value });
  };

  return (
    <div className="container mt-5">
      <Form>
        <Form.Group>
          <Form.Label>Task Name</Form.Label>
          <Form.Control type="text" value={taskName} readOnly />
        </Form.Group>
        <Form.Group>
          <Form.Label>Name of Work</Form.Label>
          <Form.Control
            type="text"
            name="nameOfWork"
            value={activity.nameOfWork}
            onChange={handleChange}
          />
        </Form.Group>
        <Form.Group>
          <Form.Label>Type of Unit</Form.Label>
          <Form.Control
            type="text"
            value={typeOfUnit} readOnly />
        </Form.Group>
        <Form.Group>
          <Form.Label>Unit Rate</Form.Label>
          <Form.Control
            type="number"
            value={unitRate} readOnly />
        </Form.Group>
        <Form.Group>
          <Form.Label>No of Units</Form.Label>
          <Form.Control
            type="number"
            name="noOfUnits"
            value={activity.noOfUnits}
            onChange={handleChange}
          />
        </Form.Group>
        <Form.Group>
          <Form.Label>Total Cost</Form.Label>
          <Form.Control
            type="number"
            name="totalCost"
            value={activity.totalCost}
            onChange={handleChange}
          />
        </Form.Group>
        <Form.Group>
          <Form.Label>Beneficiary Contribution</Form.Label>
          <Form.Control
            type="number"
            name="beneficiaryContribution"
            value={activity.beneficiaryContribution}
            onChange={handleChange}
          />
        </Form.Group>
        <Form.Group>
          <Form.Label>Grant Amount</Form.Label>
          <Form.Control
            type="number"
            name="grantAmount"
            value={activity.grantAmount}
            onChange={handleChange}
          />
        </Form.Group>
        <Form.Group>
          <Form.Label>Year of Sanction</Form.Label>
          <Form.Control
            type="number"
            name="yearOfSanction"
            value={activity.yearOfSanction}
            onChange={handleChange}
          />
        </Form.Group>
        <Button variant="primary" onClick={handleSave}>
          Save
        </Button>
      </Form>
    </div>
  );
}

export default ActivityIframe;
