import { useQueryClient, useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import { fetchStudentById } from './FetchStudentById';
import { deleteStudentById } from './DeleteStudentById';
import { makePaymentById } from './MakePaymentById';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { useEffect } from 'react';

import '../css/Button.css'
import '../css/StudentProfile.css'

function StudentProfile(){

    const { id } = useParams();

    const navigate = useNavigate();

    

    const queryClient = useQueryClient();

    const {mutate, data, isPending, error: deleteError} = useMutation({
        mutationFn: () => deleteStudentById(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["students"] });
        }
    })

    const {mutate:mutatePayment, data:paymentData, isPending:isPaymentPending, error: paymentError} = useMutation({
        mutationFn: () => makePaymentById(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["student", id] });
            queryClient.invalidateQueries({ queryKey: ["students"] });
        },
        onError: (err) => {
            console.error("Payment failed", err);
        }
    })


const {data: student, isLoading,isError, error} = useQuery({
        queryKey:["student", id],
        queryFn: () => fetchStudentById(id),
    });

     

    const updateData = () => {

       

        console.log("Student data: ",student);

        
        
       navigate(`/students/update/${id}`);
    
    }

      useEffect(() => {
        if(data){
            navigate("/")
        }
    }, [data, navigate])

    useEffect(() => {
        if(paymentData){
            // Handle successful payment - maybe show success message or update UI
            console.log("Payment successful", paymentData);
        }
    },[paymentData])


    if (isLoading) return <p style={{textAlign:'center'}}> Loading students...</p>
    if (isError) return <p  style={{textAlign:'center'}}>Failed to load students data!</p>

  

    return (
        <div>
            
                <h2 className='profile-name'>{student?.name || 'Unknown Student'}</h2>
                
                
                      <div>
                            <div className='field-row'><span className='field-label'>ID:  </span><span className='field-value'>{student?.id || 'N/A'}</span></div>
                            
                            <div className='field-row'><span className='field-label'>Age:  </span><span className='field-value'>{student?.age || 'N/A'}</span></div>
                           <div className='field-row'> <span className='field-label'>Email:  </span><span className='field-value'>{student?.email || 'N/A'}</span></div>
                           <div className='field-row'><span className='field-label'>Phone:  </span><span className='field-value'>{student?.phoneNumber || 'N/A'}</span></div>
                           <div className='field-row'><span className='field-label'>Address:  </span><span className='field-value'>{student?.address || 'N/A'}</span></div>
                           <div className='field-row'><span className='field-label'>Activities Enrolled:  </span><span className='field-value'>{
                                        student?.activitiesEnrolled?.join(', ') || 'N/A'}
                                    </span></div>
                           <div className='field-row'><span className='field-label'>Role:  </span><span className='field-value'>{student?.studentRole || 'N/A'}</span></div>
                           <div className='field-row'><span className='field-label'>Govt ID:  </span><span className='field-value'>{student?.govtIdProof || 'N/A'}</span></div>
                           <div className='field-row'><span className='field-label'>Student Type:  </span><span className='field-value'>{student?.studentType || 'N/A'}</span></div>
                        

                        </div>

                        <div style={{alignContent:'center', display:'flex', justifyContent: 'center', gap:'20px'}}>
                            { localStorage.getItem('role') === 'ADMIN' &&
                            <button className='regular-button' disabled={isPending} onClick={() => mutate()}>
                                {isPending ? 'Deleting...' : 'Delete'}
                            </button>
                            }
                            <button className='regular-button' disabled={isPaymentPending || student?.verified} onClick={() => mutatePayment()}>
                                {isPaymentPending ? 'Processing...' : 'Pay'}
                            </button>
                            <button className='regular-button' disabled={isPaymentPending || isPending} onClick={updateData}>
                                Update
                            </button>
                        </div>

                        <div>
                            {deleteError && <p style={{color: 'red', textAlign: 'center'}}>Failed to delete student: {deleteError.message}</p>}
                            {paymentError && <p style={{color: 'red', textAlign: 'center'}}>Payment failed: {paymentError.message}</p>}
                            <div className='status-verify'>
                                <span>
                                    {student?.verified ? 
                                        <p style={{color:'darkcyan'}}>Student is verified successfully ✔</p> : 
                                        <p style={{color:'green'}}>Under Verification ⏳</p>
                                    }
                                </span>
                            </div>
                        </div>
            
        </div>
    );
}

export default StudentProfile;