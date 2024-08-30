import React, { useState, useEffect } from 'react';
import { Form, Button, Container } from 'react-bootstrap';
import ActivityIframe from './ActivityIframe';
import { getVerticals, getComponents, getActivities, getTasks, saveConfiguration } from '../DataCenter/apiService';

const BeneficiaryForm = ({ projects, addBeneficiary }) => {
  const [beneficiary, setBeneficiary] = useState({
    beneficiary: '',
    fatherHusbandName: '',
    village: '',
    mandal: '',
    district: '',
    state: '',
    aadhar: '',
    surveyNo: '',
  });

  const [verticals, setVerticals] = useState([]); // Ensure this is an array
  const [components, setComponents] = useState([]);
  const [activities, setActivities] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [selectedVertical, setSelectedVertical] = useState('');
  const [selectedComponent, setSelectedComponent] = useState('');
  const [selectedActivity, setSelectedActivity] = useState('');
  const [selectedTask, setSelectedTask] = useState('');
  const [showActivityDropdown, setShowActivityDropdown] = useState(false);
  const [showTaskDropdown, setShowTaskDropdown] = useState(false);
  const [showIframe, setShowIframe] = useState(false);
  const [taskDetails,setTaskDetails]=useState([]);


  useEffect(() => {
    async function fetchVerticals() {
      const data = await getVerticals();
      setVerticals(Array.isArray(data) ? data : []); // Ensure the data is an array
    }
    fetchVerticals();
  }, []);

  useEffect(() => {
    async function fetchComponents() {
      const data = await getComponents(selectedVertical);
      console.log('Fetched components:',data);
      setComponents(Array.isArray(data) ? data : []); // Ensure the data is an array
    }
    fetchComponents();
    
  }, [selectedVertical]);

  useEffect(() => {
    async function fetchActivities() {
      if (!selectedComponent) return; // Ensure selectedComponent has a value
      try {
        console.log("Attempting to fetch activities for:", selectedComponent);
        const data = await getActivities(selectedComponent);
        console.log("Fetched activities data:", data); // Check what data is being returned
        setActivities(Array.isArray(data) ? data : []); // Ensure data is an array
      } catch (error) {
        console.error("Error fetching activities:", error);
      }
    }
    fetchActivities();
  }, [selectedComponent]);

  useEffect(() => {
    async function fetchTasks() {
      const data = await getTasks(selectedActivity);
      setTasks(Array.isArray(data) ? data : []); // Ensure the data is an array
    }
    fetchTasks(); 
  }, [selectedActivity]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setBeneficiary({ ...beneficiary, [name]: value });
  };

  const handleSaveBeneficiary = async () => {
    // const { projectName,
    //   beneficiary,
    //   fatherHusbandName,
    //   village,
    //   mandal,
    //   district,
    //   state,
    //   aadhar,
    //   surveyNo,
    //   components } = beneficiary;
    const projectConfig = {
      verticalName: selectedVertical,
      ...beneficiary,
      componentName: selectedComponent,
      activityName: selectedActivity,
      taskName: selectedTask,
      ...taskDetails
    };

    console.log(projectConfig);
    // await saveConfiguration(projectConfig);

    // Clear inputs and selected states
    setBeneficiary({
      projectName: '',
      beneficiary: '',
      fatherHusbandName: '',
      village: '',
      mandal: '',
      district: '',
      state: '',
      aadhar: '',
      surveyNo: '',
      components: '',

    })
    setSelectedVertical('');
    setSelectedComponent('');
    setSelectedActivity('');

    alert('Beneficiary saved successfully!');
  };

  const handleComponentSelect=(e)=>{
    setSelectedComponent(e.target.value);
    setShowActivityDropdown(true);
  }

  const handleActivitySelect=(e)=>{
    setSelectedActivity(e.target.value);
    setShowTaskDropdown(true);
  }


  const handleTaskSelect = (e) => {
    setSelectedTask(e.target.value);
    setShowIframe(true);
  };

  const handleAddTask=(task)=>{
    setTaskDetails(task);
  }

  const handleSubmit = () => {
    addBeneficiary(beneficiary);
  };

  return (
    <Container>
      <Form>
        <Form.Group>
          <Form.Label>Project Name</Form.Label>
          <Form.Control as="select" name="projectName" onChange={(e)=>setSelectedVertical(e.target.value)}>
            <option>Select Project</option>
            {verticals.map((project) => (
              <option key={project.id} value={project.verticalName}>
                {project.verticalName}
              </option>
            ))}
          </Form.Control>
        </Form.Group>
        <Form.Group>
          <Form.Label>Beneficiary Name</Form.Label>
          <Form.Control name="beneficiary" placeholder="Beneficiary Name" onChange={handleChange} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Father/Husband Name</Form.Label>
          <Form.Control name="fatherHusbandName" placeholder="Father/Husband Name" onChange={handleChange} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Village</Form.Label>
          <Form.Control name="village" placeholder="Village" onChange={handleChange} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Mandal</Form.Label>
          <Form.Control name="mandal" placeholder="Mandal" onChange={handleChange} />
        </Form.Group>
        <Form.Group>
          <Form.Label>District</Form.Label>
          <Form.Control name="district" placeholder="District" onChange={handleChange} />
        </Form.Group>
        <Form.Group>
          <Form.Label>State</Form.Label>
          <Form.Control name="state" placeholder="State" onChange={handleChange} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Aadhar</Form.Label>
          <Form.Control name="aadhar" placeholder="Aadhar" onChange={handleChange} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Survey No.</Form.Label>
          <Form.Control name="surveyNo" placeholder="Survey No." onChange={handleChange} />
        </Form.Group>
        <Form.Group>
          <Form.Label>Component</Form.Label>
          <Form.Control as="select" value={selectedComponent} onChange={handleComponentSelect}>
            <option>Select Component</option>
            {components.map((component) => (
              <option key={component.id} value={component.componentName}>
                {component.componentName}
              </option>
            ))}
          </Form.Control>
        </Form.Group>

        {showActivityDropdown && (
          <Form.Group>
            <Form.Label>Add Activity</Form.Label>
            <Form.Control as="select" value={selectedActivity} onChange={handleActivitySelect}>
              <option>Select Activity</option>
              {activities.map((activity) => (
                <option key={activity.id} value={activity.activityName}>
                  {activity.activityName}
                </option>
              ))}
            </Form.Control>
          </Form.Group>
        )}

        {showTaskDropdown && (
          <Form.Group>
            <Form.Label>Add Task</Form.Label>
            <Form.Control as="select" value={selectedTask} onChange={handleTaskSelect}>
              <option>Select Task</option>
              {tasks.map((task) => (
                <option key={task.id} value={task.name}>
                  {task.name}
                </option>
              ))}
            </Form.Control>
          </Form.Group>
        )}

        {showIframe && <ActivityIframe taskName={selectedTask} onSave={handleAddTask} />}

      </Form>

      <Button variant="primary" onClick={handleSaveBeneficiary}>
        Save Beneficiary
      </Button>
    </Container>
  );
};

export default BeneficiaryForm;
