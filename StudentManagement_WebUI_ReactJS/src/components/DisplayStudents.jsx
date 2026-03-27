import { Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { fetchStudents } from './FetchStudents';

function DisplayStudents() {

  

    const {data: studentData, isLoading,isError, error} = useQuery({
        queryKey:["students"],
        queryFn: fetchStudents,
        staleTime: 0,
        refetchOnWindowFocus: false,
        refetchOnReconnect: false,
        refetchOnMount: "stale"
    });

    if (isLoading) return <p style={{textAlign:'center'}}> Loading students...</p>
    if (isError) return <p  style={{textAlign:'center'}}>Failed to load students data!</p>

    return (
        <div>
            <table border="1" style={{textAlign:'center'}}>
                <thead>
                    <tr>
                        <th>Student ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone Number</th>
                        <th>Address</th>
                        <th>Verified</th>
                    </tr>
                </thead>
                <tbody>
                    {studentData?.map(student => (
                        <tr key={student.id}>
                            <td><Link to={`students/${student.id}`}>{student.id}</Link></td>
                            <td>{student.name} {student.lastName}</td>
                            <td>{student.email}</td>
                            <td>{student.phoneNumber}</td>
                            <td>{student.address}</td>
                            <td>{student.verified? "✔" : "❌"}</td>

                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default DisplayStudents;