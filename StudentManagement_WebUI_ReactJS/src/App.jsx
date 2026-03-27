import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Unprotectedroute from './components/Unprotectedroute';
import HomePage from './components/HomePage';
import StudentProfile from './components/StudentProfile';
import StudentForm from './components/StudentForm';
import Notifications from './components/Notifications';
import { RegisterForm } from './components/RegisterForm';
import Protectedroute from './components/Protectedroute';
import { LoginForm } from './components/LoginForm';
import { UpdateStudent } from './components/UpdateStudent';

function App() {
  
  return (
    <div>
      <Router>
        <Routes>
          <Route element={<Unprotectedroute/>} >
            <Route path="/auth/login" element={<LoginForm />} />
            <Route path="/auth/register" element={<RegisterForm />} />
          </Route>
          <Route path="/" element={<Protectedroute />}>
            <Route index element={<HomePage />} />
            <Route path="students/:id" element={<StudentProfile />} />
            <Route path="register" element={<StudentForm />} />
            <Route path="notifications" element={<Notifications />} />
            <Route path="students/update/:id" element={<UpdateStudent/>} />
          </Route>
        </Routes>
      </Router>
    </div>
  );
}

export default App;
