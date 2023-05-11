package com.riyandifirman.storyapp.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.riyandifirman.storyapp.R
import com.riyandifirman.storyapp.databinding.ActivityAddStoryBinding
import com.riyandifirman.storyapp.response.AddStoryResponse
import com.riyandifirman.storyapp.settings.*
import com.riyandifirman.storyapp.ui.main.MainActivity
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.concurrent.Executors

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var myPreference: Preferences
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    // fungsi untuk menangani hasil dari request permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Not get permission!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // fungsi untuk memeriksa apakah semua permission sudah diberikan
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreference = Preferences(this)

        // pengecekan permission
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        showLoading(false)

        // listener untuk tombol gallery
        binding.btnGallery.setOnClickListener {
            openGallery()
        }

        // listener untuk tombol camera
        binding.btnCamera.setOnClickListener {
            openCamera()
        }

        // listener untuk tombol add story
        binding.btnAdd.setOnClickListener {
            addStory()
        }
    }

    // fungsi untuk menangangi hasil dari permilihan gambar dari galeri
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile

            binding.imgView.setImageURI(selectedImg)
        }
    }

    // fungsi untuk membuka galeri dan memilih gambar
    private fun openGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose an Image")
        launcherIntentGallery.launch(chooser)
    }

    // fungsi untuk menangani hasil dari pemanggilan kamera
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            getFile = myFile

            val result = BitmapFactory.decodeFile( myFile?.path)

            binding.imgView.setImageBitmap(result)
        }
    }

    // fungsi untuk membuka kamera dan mengambil foto
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.riyandifirman.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    // fungsi untuk menambahkan cerita baru
    private fun addStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            showLoading(true)
            uploadImageToServer(imageMultipart)
        } else {
            Toast.makeText(this@AddStoryActivity, "Insert an image before!", Toast.LENGTH_SHORT).show()
        }
    }

    // fungsi untuk mengunggah gambar cerita ke server
    private fun uploadImageToServer(img: MultipartBody.Part){
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val description = binding.edAddDescription.text.trim().toString().toRequestBody("text/plain".toMediaType())
        scope.launch {
            val token = "Bearer ${myPreference.getUserToken()}"
            withContext(Dispatchers.Main) {
                val client = ApiConfig.getApiService().uploadStory(token,img,description)
                client.enqueue(object: Callback<AddStoryResponse> {
                    override fun onResponse(
                        call: Call<AddStoryResponse>,
                        response: Response<AddStoryResponse>
                    ) {
                        if(response.isSuccessful){
                            val responseBody = response.body()
                            if (responseBody!=null && !responseBody.error){
                                Intent(
                                    this@AddStoryActivity,
                                    MainActivity::class.java
                                ).also { intent ->
                                    intent.putExtra(MainActivity.SUCCESS_ADD_STORY, true)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                }
                            }
                        } else {
                            Toast.makeText(this@AddStoryActivity, response.message(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                        Toast.makeText(this@AddStoryActivity, "onFailure: ${t.message.toString()}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    // fungsi untuk menampilkan loading
    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE }

    // companion object untuk menyimpan properti dan konstanta yang digunakan di activity ini
    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}