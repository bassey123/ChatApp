package com.example.chatapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.chatapp.model.User
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.lang.Exception
import kotlin.collections.HashMap

class EditProfileActivity : AppCompatActivity() {

    private lateinit var reference: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var storageReference: StorageReference
    private val IMAGE_REQUEST = 1
    private var imageUrl: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        storageReference = FirebaseStorage.getInstance().getReference("uploads")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                val user: User = p0.getValue(User::class.java)!!
                edit_user.setText(user.userName)
                edit_reg.setText(user.regNo)
                edit_email.setText(user.email)
                if (user.imageURL == "default") {
                    edit_profileImage.setImageResource(R.drawable.ic_person)
                } else {
                    Glide.with(applicationContext)
                        .load(user.imageURL)
                        .into(edit_profileImage)
                }
            }
        })

        edit_profileImage.setOnClickListener {
            openImage()
        }

    }

    private fun openImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST)
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val mimeTypeMap: MimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadImage() {
        val pd = ProgressDialog(this@EditProfileActivity)
        pd.setMessage("Uploading")
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        pd.show()

        if (imageUrl != null) {
            val fileReference = storageReference.child(System.currentTimeMillis().toString()
                    + "." + getFileExtension(imageUrl!!))

            val uploadTask: UploadTask = fileReference.putFile(imageUrl!!)
            uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                override fun then(p0: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                    if (!p0.isSuccessful) {
                        throw p0.exception!!
                    }
                    return fileReference.downloadUrl
                }
            }).addOnCompleteListener(object : OnCompleteListener<Uri> {
                override fun onComplete(p0: Task<Uri>) {
                    if (p0.isSuccessful) {
                        val downloadUri = p0.result
                        val mUri = downloadUri.toString()

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
                        val map = HashMap<String, Any>()
                        map["imageURL"] = mUri
                        reference.updateChildren(map)

                        pd.dismiss()
                    } else {
                        Toast.makeText(applicationContext, "Failed!", Toast.LENGTH_SHORT).show()
                        pd.dismiss()
                    }
                }
            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                    pd.dismiss()
                }
            })
        } else {
            Toast.makeText(applicationContext, "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fileReference = storageReference.child(System.currentTimeMillis().toString()
                + "." + imageUrl?.let { getFileExtension(it) })
        val uploadTask: UploadTask? = imageUrl?.let { fileReference.putFile(it) }

        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null) {
            imageUrl = data.data!!

            if (uploadTask != null && uploadTask.isInProgress) {
                Toast.makeText(applicationContext, "Upload in Progress", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
