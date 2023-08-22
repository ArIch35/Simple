import {useRef} from 'react'
import classes from './MeetupForm.module.css'
import Card from '../components/ui/Card';
function MeetupForm(props){
    const titleInput = useRef();
    const imgInput = useRef();
    const addressInput = useRef();
    const descInput = useRef();

    function dec2hex (dec) {
        return dec.toString(16).padStart(2, "0")
    }
      
    function generateId (len) {
        var arr = new Uint8Array((len || 40) / 2)
        window.crypto.getRandomValues(arr)
        return Array.from(arr, dec2hex).join('')
    }

    function submitHandler(event){
        event.preventDefault();

        const enteredTitle = titleInput.current.value;
        const enteredImage = imgInput.current.value;
        const enteredAddress = addressInput.current.value;
        const enteredDesc = descInput.current.value;

        const data = {
            id: generateId(10),
            title: enteredTitle,
            img_src: enteredImage,
            address: enteredAddress,
            desc: enteredDesc
        }

        props.addMeetupHandler(data)
    }

    return <form className={classes.form} onSubmit={submitHandler}>
        <Card>
        <div className={classes.holder}>
            <label className={classes.lab} htmlFor='title'>Title</label>
            <input type='text' id='title' name='title' className={classes.inp} required ref={titleInput}></input>
        </div>
        <div className={classes.holder}>
            <label className={classes.lab} htmlFor='img'>Image Link</label>
            <input type='text' id='img' name='img' className={classes.inp} required ref={imgInput}></input>
        </div>
        <div className={classes.holder}>
            <label className={classes.lab} htmlFor='add'>Address</label>
            <input type='text' id='add' name='add' className={classes.inp} required ref={addressInput}></input>
        </div>
        <div className={classes.holder}>
            <label className={classes.lab} htmlFor='desc'>Description</label>
            <textarea id='desc' name='desc' className={classes.area} ref={descInput}></textarea>
        </div>
        <div className={classes.holder_left}>
            <button className={classes.btn}>Add Info</button>
        </div>
        </Card>
    </form>
}

export default MeetupForm;