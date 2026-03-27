function StudentList({ students, deleteStudent }) {

    return(
        <ul>
        {students.map(student => (
          <li key={student.id}>{student.name} {student.age} {student.email} {student.phoneNumber} {student.address} - {student.activitiesEnrolled}
          <button style = {{marginLeft: "10px"}} onClick={() => deleteStudent(student.id)}>Delete</button></li>
        ))}
      </ul>
    );
}

export default StudentList;