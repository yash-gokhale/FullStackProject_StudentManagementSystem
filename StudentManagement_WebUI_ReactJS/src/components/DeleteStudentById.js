export const deleteStudentById = async (id) => {
            const token = localStorage.getItem("token")

            const response = await fetch(`http://localhost:8081/students/remove/${id}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });


            if (!response.ok) {
                throw new Error("Failed to load student data!")
            }

            const result = await response.json();
            return result.data;
  }