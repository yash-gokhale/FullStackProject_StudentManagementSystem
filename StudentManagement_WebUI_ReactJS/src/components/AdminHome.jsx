import DisplayStudents from "./DisplayStudents";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";


function Home() {

  const navigate = useNavigate();

useEffect(() => {
  const timeout = setTimeout(() => {
    alert("Session expired");
      localStorage.removeItem("token");
      navigate("/auth/login");
  }, 1000 * 60 * 60 * 12);

  return () => clearTimeout(timeout);
}, []);

  return (
    <div>
      <h1>Welcome to the Admin Home Page!</h1>
      
          <div>
            <p style={{paddingLeft:'300px'}}>Below is the list of registered students.</p>
            <DisplayStudents />
      </div>
    </div>
  );
}

export default Home;