export const updateStudentData = async (student) => {

            const token = localStorage.getItem('token');
            const response = await fetch(`http://localhost:8081/students/update`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(student)
            });


            if (!response.ok) {
                throw new Error("Failed to load student data!")
            }

            const result = await response.json();
            return result;
  }