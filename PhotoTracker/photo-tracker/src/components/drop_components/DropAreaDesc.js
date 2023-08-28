import classes from './DropAreaDesc.module.css'

function DropAreaDesc(props){
    const highlightStatus = !props.higlighted ? classes.desc: classes.desc_highlight

    return <div className={classes.container_desc} >
        <p className={highlightStatus}>Drop Here.....</p>
        <p className={highlightStatus}>Or</p>
        <button className={!props.higlighted ? classes.btn : classes.btn_highlight}
        onClick={() => document.getElementById('imageInput').click()}>Select Image</button>
        <p className={highlightStatus}>{props.filename}</p>
    </div>
}

export default DropAreaDesc;