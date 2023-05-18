package com.example.database;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.database.model.ItemModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;


public class AddItem extends Fragment {

    Spinner categorySpinner,
            providerSpinner,itemType;
    EditText inputName,inputPrice,inputDescription;
    Button btnUpload;
    ImageButton imgSelectBtn;
    ImageView itemImgView;
    ActivityResultLauncher<Intent> resultLauncher;
    Uri imgURI,downloadUri;
    String category,provider,itemName,description,type;
    Double itemPrice;
    int rating;
    String documentId;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    public AddItem() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        categorySpinner = view.findViewById(R.id.category_spinner);
        providerSpinner = view.findViewById(R.id.provider_spinner);
        inputDescription = view.findViewById(R.id.description_input);
        inputName = view.findViewById(R.id.item_name);
        inputPrice = view.findViewById(R.id.input_price);
        btnUpload = view.findViewById(R.id.btn_upload);
        itemImgView = view.findViewById(R.id.item_img);
        imgSelectBtn = view.findViewById(R.id.choose_img);
        itemType = view.findViewById(R.id.type_spinner);
        initialize();
        return view;
    }
    private void initialize() {
        //registering activityresultLaucher
        imgURI = null;
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            imgURI = data.getData();
                            changeImageView();
                        }

                    }
                });
       
        imgSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                resultLauncher.launch(intent);

            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputValues();
            }
        });

    }

    private void checkInputValues() {
        //check if all inputs are correct
        category = categorySpinner.getSelectedItem().toString();
        provider = providerSpinner.getSelectedItem().toString();
        itemName = inputName.getText().toString();
        description = inputDescription.getText().toString();
        type = itemType.getSelectedItem().toString();
        if(!inputPrice.getText().toString().isEmpty()){
            itemPrice = Double.parseDouble(inputPrice.getText().toString());
        }
        else{
            Toast.makeText(getActivity(), "Please fill Item Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(category.toLowerCase().equals("select")||provider.toLowerCase().equals("select")){
            Toast.makeText(getActivity(), "Please Select category and provider", Toast.LENGTH_LONG).show();
            return;
        }
        if(type.toLowerCase().equals("select")){
            Toast.makeText(getActivity(), "Please select Item type", Toast.LENGTH_SHORT).show();
        }
        else if (itemName.isEmpty()){
            Toast.makeText(getActivity(), "Please fill Item Name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(description.isEmpty()||description.length()<10){
            Toast.makeText(getActivity(), "Description is needed at least 10 character", Toast.LENGTH_LONG).show();
        }
        else if(imgURI==null){
            Toast.makeText(getActivity(), "Please select Image", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("uploading");
        progressDialog.setCancelable(false);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
        t.run();
        Toast.makeText(getActivity(), "processing", Toast.LENGTH_SHORT).show();
        upLoadToFirebase();
    }

    private void upLoadToFirebase() {
        //first upload Image
        uploadImageToFireStore();
        //then upload item
    }

    private void uploadImageToFireStore() {
        UploadTask uploadTask;
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference ref = storageRef.child("images/"+imgURI.getLastPathSegment()+ FieldValue.serverTimestamp());
        uploadTask = ref.putFile(imgURI);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_LONG).show();
                }
                else{
                    // Continue with the task to get the download URL
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_LONG).show();
                    downloadUri = task.getResult();
                    uploadItem();
                } else {
                    Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    resetInput();
                }
            }
        });


    }

    private void uploadItem() {
//        Map<String,Object> item = new HashMap<>();
//        item.put("itemName",itemName);
//        item.put("itemPrice",itemPrice);
//        item.put("category",category);
//        item.put("provider",provider);
//        item.put("rating",rating);
//        item.put("description",description);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Error Ocurred");
        builder.setMessage("Image is not uploaded Please try again").setCancelable(false);
        builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog = builder.create();
        ItemModel item = new ItemModel();
                item.setItemName(itemName);
                item.setItemPrice(itemPrice);
                item.setCategory(category);
                item.setProvider(provider);
                item.setDescription(description);
                item.setType(type);
        if(downloadUri!=null){
            item.setImgUri(downloadUri.toString());
            //then upload item
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("itemcollections").add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    documentId = documentReference.getId();
                    Toast.makeText(getActivity(), "uploaded"+documentReference.getId(), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            alertDialog.show();

        progressDialog.dismiss();
        resetInput();
    }
    private void resetInput() {
        categorySpinner.setSelection(0);
        providerSpinner.setSelection(0);
        itemType.setSelection(0);
        inputName.setText("");
        downloadUri = null;
        imgURI = null;
        inputDescription.setText("");
        inputPrice.setText("");
        itemImgView.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.c7));
    }
    private void changeImageView() {
        itemImgView.setImageURI(imgURI);
    }
}