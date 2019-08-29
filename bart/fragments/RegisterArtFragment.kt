package com.bart.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.bart.R
import com.bart.api.AppController
import com.bart.api.Responce
import com.bart.api.interfaces.ApiService
import com.bart.interfaces.NavigationHost
import com.bart.interfaces.SelectViewPager
import com.bart.utility.GlideApp
import com.bart.utility.ProgressRequestBody
import com.bart.utility.Utility
import kotlinx.android.synthetic.main.register_your_art_fragment.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RegisterArtFragment  : Fragment() , View.OnClickListener {
    @Inject
    internal lateinit var apiService: ApiService

    private var ctx: Context? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private var image_ok: Boolean? = false

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0

    var mCurrentPhotoPath: String? = null

    private var date: String? = null


    internal var qrScanIntegrator: IntentIntegrator? = null

    companion object {
        fun newInstance(user_type: Int): RegisterArtFragment {
            val fragment = RegisterArtFragment()
            val args = Bundle()
            args.putInt("user_type", user_type)
            fragment.arguments = args
            return fragment
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.card_view_take_photo -> {
                image_ok = false

                dispatchTakePictureIntent()
            }
            R.id.card_view_qr_scanner -> {
                performAction()
            }
            R.id.btnReg -> {
                validate()
            }
            R.id.tvAlreadyAcct -> {
                ((ctx!! as Activity as NavigationHost).navigateTo(SigninFragment.newInstance(0), false, false))
            }
            R.id.et_purchage_date -> {
                val datePickerDialog = DatePickerDialog(ctx!!,
                        DatePickerDialog.OnDateSetListener { datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
                            date = "" + i + "-" + (i1 + 1) + "-" + i2
                            et_purchage_date.setText("" + i2 + "-" + (i1 + 1) + "-" + i)

                        }, mYear, mMonth, mDay)
                datePickerDialog.show()
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            }

        }

    }

    private fun performAction() {
        qrScanIntegrator?.initiateScan()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get the custom view for this fragment layout

        return inflater.inflate(R.layout.register_your_art_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qrScanIntegrator = IntentIntegrator.forSupportFragment(this)
        qrScanIntegrator?.setBarcodeImageEnabled(true)
        qrScanIntegrator?.setOrientationLocked(false)
        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH) + 1
        mDay = c.get(Calendar.DAY_OF_MONTH)

        date = "$mYear-$mMonth-$mDay"
        card_view_take_photo.setOnClickListener(this)
        card_view_qr_scanner.setOnClickListener(this)

        btnReg.setOnClickListener(this)
        et_purchage_date.setOnClickListener(this)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.ctx = context
        ((context!! as Activity).application as AppController).component.inject(this@RegisterArtFragment)
    }

    private fun validate() {
        if (image_ok!!) {
            if (!TextUtils.isEmpty(et_painter_name.text)) {
                if (!TextUtils.isEmpty(et_owner_name.text)) {
                    if (!TextUtils.isEmpty(et_gallery_name.text.toString())) {
                        if (!TextUtils.isEmpty(et_purchage_date.text)) {
                            if (!TextUtils.isEmpty(et_art_value.text)) {
                                if (Utility.isConnected()) {
                                    register()
                                } else {
                                    Toast.makeText(activity, "No internet connection!", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(activity, "Please enter art value!", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            Toast.makeText(activity, "Please enter purchage date!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, "Please enter gallery name!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Please enter owner name!", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(activity, "Please enter painter name!", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(ctx as Activity, "Please take Picture of art!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun register() {

        progressBar.visibility = View.VISIBLE

        val addart = RequestBody.create(MediaType.parse("text/plain"), "addart")
        val user_id = RequestBody.create(MediaType.parse("text/plain"), AppController.getSharedPref().getString("user_id", "null")!!)
        val qr = RequestBody.create(MediaType.parse("text/plain"), "qr")
        val et_painter_name = RequestBody.create(MediaType.parse("text/plain"), et_painter_name.text.toString())

        val et_owner_name = RequestBody.create(MediaType.parse("text/plain"), et_owner_name.text.toString())
        val et_gallery_name = RequestBody.create(MediaType.parse("text/plain"), et_gallery_name.text.toString())
        val et_purchage_date = RequestBody.create(MediaType.parse("text/plain"), date!!)
        val et_art_value = RequestBody.create(MediaType.parse("text/plain"), et_art_value.text.toString())

        val art_type = RequestBody.create(MediaType.parse("text/plain"), "")

        apiService.addArt(addart, user_id, prepareFilePart("artpic", File(mCurrentPhotoPath)), qr, et_painter_name, et_owner_name, et_gallery_name, et_purchage_date, et_art_value, art_type)
                .enqueue(object : Callback<Responce.Status> {
                    override fun onResponse(call: Call<Responce.Status>, response: Response<Responce.Status>) {
                        if (response.isSuccessful && response.body()!!.status) {
                            progressBar.visibility = View.GONE
                            (ctx!! as Activity as SelectViewPager).currentItem(1)
                        } else {
                            progressBar.visibility = View.GONE
                            Toast.makeText(ctx as Activity, response.body()!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Responce.Status>, t: Throwable) {
                        t.printStackTrace()

                        progressBar.visibility = View.GONE
                    }
                })
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity((ctx as Activity).packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            ctx!!,
                            "${ctx!!.packageName}.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File = (ctx as Activity).getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            ivArt.visibility = View.VISIBLE
            GlideApp.with(this@RegisterArtFragment).load(mCurrentPhotoPath).into(ivArt)
            image_ok = true
        } else {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                // If QRCode has no data.
                if (result.contents == null) {
                    Toast.makeText(ctx as Activity, "Result not found!", Toast.LENGTH_LONG).show()
                } else {
                    // If QRCode contains data.
                    try {
                        val dd = result.contents
                        // Converting the data to json format
                        //  val obj = JSONObject(result.contents)

                        et_owner_name!!.setText(AppController.getSharedPref().getString("user_name", ""))

                        val qrData = dd.toString().split("\n")

                        val name = qrData[0].split("Name: ")[1]
                        et_gallery_name!!.setText(name)

                        val artist = qrData[1].split("Artist : ")[1]
                        et_painter_name!!.setText(artist)

                        val price = qrData[8].split("Price : ₹")[1]
                        et_art_value!!.setText(price)
                        /* val size  = qrData[2].split("Size :  ")[1]
                       val medium =qrData[3].split("Medium :  ")[1]
                       val surface = qrData[5].split("Surface :  ")[1]
                       val artwork = qrData[6].split("Artwork:  ")[1]
                       val sku = qrData[6].split("SKU :  ")[1]
                       val created_in  = qrData[7].split("Created in  ")[1]+"-0-0"
                       val private_key  = qrData[9].split("Private Key : ")[1]
                       val description = qrData[10].split("Description : ")[1]*/
                    } catch (e: JSONException) {
                        e.printStackTrace()

                        // Data not in the expected format. So, whole object as toast message.
                        // Toast.makeText(activity, result.contents, Toast.LENGTH_LONG).show()
                    }

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
    private fun prepareFilePart(param: String, file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(param, file.name, ProgressRequestBody(file, object : ProgressRequestBody.UploadCallbacks {
            override fun onProgressUpdate(percentage: Int) {}
            override fun onError() {}
            override fun onFinish() {}
        }))
    }
}

