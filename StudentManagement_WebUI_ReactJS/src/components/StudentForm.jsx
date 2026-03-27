import { useState } from "react";

import { useNavigate } from "react-router-dom";

function StudentForm({}) {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [age, setAge] = useState("");
    const [email, setEmail] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [address, setAddress] = useState("");
    const [activityInterests, setActivity] = useState([]);
    const [govtIdProof, setGovtIdProof] = useState("");
    const [testScore, setTestScore] = useState("");

    const navigate = useNavigate();
    

    const handleSubmit = async () => {
        if (!firstName || !lastName || !activityInterests.length || !govtIdProof) {
            alert("Please fill in all required fields, including Government ID Proof and at least one activity.");
            return;
        }


        const student = {
            firstName,
            lastName,
            age,
            email,
            phoneNumber,
            address,
            activityInterests: activityInterests.join(', '),
            govtIdProof,
            testScore
         };

         try{
            const response = await fetch("http://localhost:8081/students",{
                method : "POST",
                headers : {
                    "Content-Type" : "application/json"
                },
                body : JSON.stringify(student)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const savedStudent = await response.json();

            const msg = savedStudent.message;

          

            setFirstName("");
            setLastName("");
            setAge("");
            setEmail("");
            setPhoneNumber("");
            setAddress("");
            setActivity([]);
            setGovtIdProof("");
            setTestScore("");

            navigate('/');


         }
         catch(error){
            console.error("Error adding student:", error);
         }
        
    }

    return ( <div>
      <div style={{marginBottom : "20px"}}>
        <input placeholder="Student First name" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
      </div>
      <div style={{marginBottom : "20px"}}>
        <input placeholder="Student Last name" value={lastName} onChange={(e) => setLastName(e.target.value)} />
      </div>
      <div style={{marginBottom : "20px"}}>
        <input placeholder="Student Age" value={age} onChange={(e) => setAge(e.target.value)} />
      </div>
      <div style={{marginBottom : "20px"}}>
        <input placeholder="Student Email" value={email} onChange={(e) => setEmail(e.target.value)} />
      </div>
      <div style={{marginBottom : "20px"}}>
        <input placeholder="Student Phone Number" value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} />
      </div>
      <div style={{marginBottom : "20px"}}>
        <input placeholder="Student Address" value={address} onChange={(e) => setAddress(e.target.value)} />
      </div>
      <div style={{marginBottom : "20px"}}>
        <label>Student Activities:</label>
        <div>
          {['SPORTS', 'MUSIC', 'ART', 'TECHNOLOGY', 'LITERATURE', 'TRAVEL', 'FOOD', 'FITNESS', 'GAMING', 'VOLUNTEERING'].map(activity => (
            <label key={activity} style={{display: 'block', marginBottom: '5px'}}>
              <input
                type="checkbox"
                value={activity}
                checked={activityInterests.includes(activity)}
                onChange={(e) => {
                  if (e.target.checked) {
                    setActivity([...activityInterests, activity]);
                  } else {
                    setActivity(activityInterests.filter(a => a !== activity));
                  }
                }}
              />
              {activity}
            </label>
          ))}
        </div>
      </div>
      <div style={{marginBottom : "20px"}}>
        <select value={govtIdProof} onChange={(e) => setGovtIdProof(e.target.value)}>
          <option value="" disabled>Select Government ID Proof</option>
          <option value="Passport">Passport</option>
          <option value="Aadhar_Card">Aadhar card</option>
          <option value="PAN_Card">Pan Number</option>
        </select>
      </div>
      <div style={{marginBottom : "20px"}}>
        <input placeholder="Test Score" min={0} max={100} value={testScore} onChange={(e) => setTestScore(e.target.value)} />
      </div>
      <button onClick={handleSubmit}>Add Student</button>

    </div>
    );


}

export default StudentForm;