import { createContext, useEffect, useState} from "react";
import { toast } from "react-toastify";

export const usercontext =createContext();

function Appcontext({children}){
    const backendURL=`http://localhost:8080/resumeAnalyser/entry/v1`
    const serviceURL=`http://localhost:8080/resumeAnalyserCore/service/v1`
    const [islogged,setislogged]=useState(false)
    const [isprevious,setisprevious]=useState(false)
    const [username,setusername]=useState("")
    const [isauthenticated,setisauthenticated]=useState(false)


   useEffect(() => {
        fetch(`${serviceURL}/isValid`, { method: "post", credentials: 'include' }).then(response => {
            if (response.ok) {
                return response.json()
                
            }
            else {
                setisauthenticated(true)
                return;
            }
        })
            .then(data => {
                if (data != null) {
                    setusername(data.username);
                    setisprevious(data.isPrevious)
                    setislogged(true)
                    setisauthenticated(true)
                }
            }).catch(error=> setisauthenticated(true))

    }, [])


    const arr ={islogged,setislogged,isprevious,setisprevious,username,setusername,backendURL,serviceURL,isauthenticated}

    return (
        <usercontext.Provider value={arr}>
            {children}
        </usercontext.Provider>
    )
}

export default Appcontext