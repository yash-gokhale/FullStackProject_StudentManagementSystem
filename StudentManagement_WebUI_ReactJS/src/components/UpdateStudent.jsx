import { useForm } from "react-hook-form";
import { useMutation, useQueryClient, useQuery } from "@tanstack/react-query";
import { updateStudentData } from "./UpdateStudentData";
import "../css/Loginform.css";
import { useParams } from "react-router-dom";
import { useEffect } from "react";

export const UpdateStudent = ({onSubmit}) => {
    const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm();
    const queryClient = useQueryClient();

    const {id} = useParams();

    const {data: student, isLoading,isError, error} = useQuery({
            queryKey:["student", id],
            queryFn: () => fetchStudentById(id),
        });

    const { mutate, data: updateData, isPending, error: registerError } = useMutation({
        mutationFn: (data) => updateStudentData(data),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["update"] });
        }
    });

            useEffect(() => {
        if (student) {
            reset({
            name: student.name,
            age: student.age,
            phone: student.phoneNumber,
            address: student.address,
            });
        }
        }, [student, reset]);

   

    const submitHandler = async (data) => {
        try {
            //const updatedData = {...data, id :student.id, email :student.email, activitiesEnrolled: student.activitiesEnrolled, studentRole: student.studentRole, govtIdProof: student.govtIdProof, verified: student.verified, registrationDate: student.registrationDate}
            const updatedData = { ...student, ...data}
            console.log("Sending new data: ",updatedData);
            mutate(updatedData);
            if (onSubmit) {
                await onSubmit(data);
            } else {
                console.log("update data: ", updatedData);
            }
        } catch (err) {
            console.error("Update failed", err);
        }

            // if(updateData && updateData.status == 200){
            //     reset();
            // }
    };

    return (
        <div className="container-style">
            <form onSubmit={handleSubmit(submitHandler)} className="form-style">
                <h2>Update Form</h2>
                <div className="field-style">
                    <label>Name</label>
                    <input
                        type="text" 
                        {...register("name", {
                            required: "name is required",
                            minLength: {
                                value: 3,
                                message: "Minimum length is 3 characters"
                            }
                        })}
                    />
                    {errors.name && (
                        <p className="error-style">{errors.name.message}</p>
                    )}
                </div>

                <div className="field-style">
                    <label>Age</label>
                    <input
                        type="number" 
                        {...register("age", {
                            required: "age is required",
                            min: {
                                value: 0,
                            },
                            max:{
                                value: 100,
                            }
                        })}
                    />
                    {errors.age && (
                        <p className="error-style">{errors.age.message}</p>
                    )}
                </div>


                <div className="field-style">
                    <label>Phone</label>
                    <input
                        type="text" 
                        {...register("phone", {
                            required: "phone is required",
                            minLength: {
                                value: 3,
                                message: "Minimum length is 3 characters"
                            }
                        })}
                    />
                    {errors.phone && (
                        <p className="error-style">{errors.phone.message}</p>
                    )}
                </div>


                <div className="field-style">
                    <label>Address</label>
                    <input
                        type="text"
                        {...register("address", {
                            required: "address is required",
                            minLength: {
                                value: 3,
                                message: "Minimum length is 3 characters"
                            }
                        })}
                    />
                    {errors.address && (
                        <p className="error-style">{errors.address.message}</p>
                    )}
                </div>

                


                <button
                    type="submit"
                    disabled={isSubmitting || isPending || isLoading}
                    className="button-style"
                >
                    {isLoading || isSubmitting || isPending ? "Loading..." : "Update"}
                </button>

            </form>
                <br />
            <div>
                {updateData && <p>{updateData.message}</p>}
            {registerError && <p className="error-style">Registration failed: {registerError.message}</p>}
            </div>

            
        </div>

    );
};