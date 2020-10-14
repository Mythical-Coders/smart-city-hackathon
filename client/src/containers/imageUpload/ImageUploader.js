import React, { useState } from "react";
import Grid from "@material-ui/core/Grid";
import Card from "@material-ui/core/Card/Card";
import CardActionArea from "@material-ui/core/CardActionArea";
import CardMedia from "@material-ui/core/CardMedia";
import Button from "../../components/CustomButtons/Button";
import Container from "@material-ui/core/Container";
import { useDispatch, useSelector } from "react-redux";
import TextField from "@material-ui/core/TextField";
import Typography from "@material-ui/core/Typography";
import { uploadImage } from "../../actions/uploadActions";
import { useHistory } from "react-router-dom";

const ImageUploader = () => {
  const dispatch = useDispatch();
  const history = useHistory()
  const [imagePreview, setImagePreview] = useState(null);
  const [imageData, setImageData] = useState(null);
  const [imageName, setImageName] = useState("");
  const { image } = useSelector((state) => state.upload);

  const handleUploadClick = (event) => {
    let file = event.target.files[0];
    const imageData = new FormData();
    imageData.append("imageFile", file);
    setImageData(imageData);
    setImagePreview(URL.createObjectURL(file));
  };

  const uploadImageWithAdditionalData = () => {
    imageData.append("imageName", imageName);
    dispatch(uploadImage(imageData)).then((res)=>{
      history.push("/admin_dashboard/images")
    }).catch(()=>{console.log("error")});
  };

  const handleChange = (event) => {
    setImageName(event.target.value);
  };

  return (
    <Container maxWidth="lg" style={{marginBottom : "10px"}}>
      <Grid container spacing={2}>
        <Grid item xs={4}>
          <Card>
            <CardActionArea>
              <CardMedia
                component="img"
                image={
                  imagePreview !== null
                    ? imagePreview
                    : "https://www.amerikickkansas.com/wp-content/uploads/2017/04/default-image.jpg"
                }
              />
            </CardActionArea>
          </Card>
          <input
            accept="image/*"
            
            id="upload-profile-image"
            type="file"
            onChange={handleUploadClick}
          />
          <label htmlFor="upload-profile-image">
            <Button
              variant="contained"
              color="chengapp"
        
              component="span"
            >
              اختر صورة
            </Button>
          </label>
          <TextField
            fullWidth
            label="عنوان الصورة"
            margin="dense"
            name="name"
         
            onChange={handleChange}
            required
            value={imageName}
            variant="outlined"
          />
          <Button
            variant="contained"
            color="chengapp"
     
            onClick={() => uploadImageWithAdditionalData()}
          >
            تحميل الصور{" "}
          </Button>
          <Typography >
            {image === null
              ? "اختر صورة لتحميلها"
              : "تم تحميل الصورة"}
          </Typography>
        </Grid>
      </Grid>
    </Container>
  );
};

export default ImageUploader;
