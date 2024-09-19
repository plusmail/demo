


async function uploadToServer(formObj){
    console.log("upload to 서버");
    console.log(formObj)
    const response = await axios({
        method: 'post',
        url: '/upload',
        data : formObj,
        header: {
            'Content-Type': 'multipart/form-data'
        },
    })
    return response.data
}

async function removeFileToServer(uuid, fileName){
    const reponse = await axios.delete(`/remove/${uuid}_${fileName}`)
}