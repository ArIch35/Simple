import FolderObject from './FolderObject'
import classes from './FolderList.module.css'
import ImageObject from '../image_component/ImageObject'

function FolderList(props){
    return <div className={classes.ctr_folder}>
             {props.list.map((element) => {
                if(props.type === 'folder') return <FolderObject img={element.uris[0]} date={element.date} itemClicked={props.itemClicked} folderType={props.folderType}/>
                if(props.imgType === 'all') return <ImageObject id={element.id} title={element.title} desc={element.desc} img={element.uri} type='all'/>
                return <ImageObject id={element.id} title={element.title} desc={element.desc} img={element.uri} date={props.date}type='fav'/>
                })}
            </div>
}

export default FolderList;