import { Navigate, Outlet, useLocation } from "react-router-dom";
import Navbar from "./Navbar";
import Footer from "./Footer";

function Protectedroute(){
    const token = localStorage.getItem("token")
    const location  = useLocation();

    if(!token){
        return <Navigate to="/auth/login" state={{from: location}} replace />
    }

    return (
        <>
            <Navbar />
            <Outlet />
            <Footer />
        </>
    );
}

export default Protectedroute;