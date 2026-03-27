export const fetchStudentById = async (id) => {

            const token = localStorage.getItem("token")
            const response = await fetch(`http://localhost:8081/students/${id}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });


            if (!response.ok) {
                throw new Error("Failed to load students data!")
            }

            const result = await response.json();
            return result.data;
  }