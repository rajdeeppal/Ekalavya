import React, { useState, useEffect } from 'react';
import { getVerticals, getComponents, getActivities, saveConfiguration } from './apiService';

function App() {
    const [verticals, setVerticals] = useState([]); // Ensure this is an array
    const [components, setComponents] = useState([]);
    const [activities, setActivities] = useState([]);
    const [selectedVertical, setSelectedVertical] = useState('');
    const [selectedComponent, setSelectedComponent] = useState('');
    const [selectedActivity, setSelectedActivity] = useState('');
    const [newVerticalName, setNewVerticalName] = useState('');
    const [newComponentName, setNewComponentName] = useState('');
    const [newActivityName, setNewActivityName] = useState(''); // New activity name state
    const [taskName, setTaskName] = useState('');
    const [units, setUnits] = useState('');
    const [ratePerUnit, setRatePerUnit] = useState('');

    useEffect(() => {
        async function fetchVerticals() {
            const data = await getVerticals();
            console.log('Fetched verticals:', data);
            setVerticals(Array.isArray(data) ? data : []); // Ensure the data is an array
        }
        fetchVerticals();
    }, []);

    useEffect(() => {
        if (selectedVertical && selectedVertical !== 'other') {
            async function fetchComponents() {
                const data = await getComponents(selectedVertical);
                setComponents(Array.isArray(data) ? data : []); // Ensure the data is an array
            }
            fetchComponents();
        }
    }, [selectedVertical]);

    useEffect(() => {
        if (selectedComponent && selectedComponent !== 'other') {
            async function fetchActivities() {
                const data = await getActivities(selectedComponent);
                setActivities(Array.isArray(data) ? data : []); // Ensure the data is an array
            }
            fetchActivities();
        }
    }, [selectedComponent]);

    const handleSave = async () => {
        const projectConfig = {
            verticalName: selectedVertical === 'other' ? newVerticalName : selectedVertical,
            componentName: selectedComponent === 'other' ? newComponentName : selectedComponent,
            activityName: selectedActivity === 'other' ? newActivityName : selectedActivity,
            taskName,
            units,
            ratePerUnit
        };
        await saveConfiguration(projectConfig);

        // Refresh dropdown data after saving
        const updatedVerticals = await getVerticals();
        setVerticals(Array.isArray(updatedVerticals) ? updatedVerticals : []); // Ensure the data is an array

        // Clear inputs and selected states
        setSelectedVertical('');
        setSelectedComponent('');
        setSelectedActivity('');
        setNewVerticalName('');
        setNewComponentName('');
        setNewActivityName(''); // Clear new activity input
        setTaskName('');
        setUnits('');
        setRatePerUnit('');

        alert('Configuration saved successfully!');
    };

    return (
        <div>
            <h1>Project Configuration</h1>

            <div>
                <label>Vertical:</label>
                <select onChange={(e) => setSelectedVertical(e.target.value)} value={selectedVertical}>
                    <option value="">Select Vertical</option>
                    {verticals.map(v => <option key={v.id} value={v.verticalName}>{v.verticalName}</option>)}
                    <option value="other">Other</option>
                </select>
            </div>

            {selectedVertical === 'other' && (
                <div>
                    <label>New Vertical Name:</label>
                    <input type="text" onChange={(e) => setNewVerticalName(e.target.value)} value={newVerticalName} />
                </div>
            )}

            {selectedVertical && (
                <div>
                    <label>Component:</label>
                    <select onChange={(e) => setSelectedComponent(e.target.value)} value={selectedComponent}>
                        <option value="">Select Component</option>
                        {components.map(c => <option key={c.id} value={c.componentName}>{c.componentName}</option>)}
                        <option value="other">Other</option>
                    </select>
                </div>
            )}

            {selectedComponent === 'other' && (
                <div>
                    <label>New Component Name:</label>
                    <input type="text" onChange={(e) => setNewComponentName(e.target.value)} value={newComponentName} />
                </div>
            )}

            {selectedComponent && (
                <div>
                    <label>Activity:</label>
                    <select onChange={(e) => {
                        setSelectedActivity(e.target.value);
                        if (e.target.value !== 'other') {
                            setNewActivityName(''); // Clear the new activity name if not 'Other'
                        }
                    }} value={selectedActivity}>
                        <option value="">Select Activity</option>
                        {activities.map(a => <option key={a.id} value={a.activityName}>{a.activityName}</option>)}
                        <option value="other">Other</option>
                    </select>
                </div>
            )}

            {selectedActivity === 'other' && (
                <div>
                    <label>New Activity Name:</label>
                    <input type="text" onChange={(e) => setNewActivityName(e.target.value)} value={newActivityName} />
                </div>
            )}

            <div>
                <label>Task Name:</label>
                <input type="text" onChange={(e) => setTaskName(e.target.value)} value={taskName} />
            </div>
            <div>
                <label>Units:</label>
                <input type="number" onChange={(e) => setUnits(e.target.value)} value={units} />
            </div>
            <div>
                <label>Rate per Unit:</label>
                <input type="number" onChange={(e) => setRatePerUnit(e.target.value)} value={ratePerUnit} />
            </div>
            <button onClick={handleSave}>Save Configuration</button>
        </div>
    );
}

export default App;
