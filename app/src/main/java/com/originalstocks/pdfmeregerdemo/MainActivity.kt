package com.originalstocks.pdfmeregerdemo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.neosoft.pdfmeregerdemo.databinding.ActivityMainBinding
import com.tom_roush.pdfbox.io.MemoryUsageSetting
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility
import java.io.File


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private val PDF_PCIKER_REQUEST_TYPE = "application/pdf"
    private var pdfFileList: ArrayList<File> = ArrayList()
    private var destFileName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.firstFilePickerButton.setOnClickListener {
            pickPDFFileOne.launch(PDF_PCIKER_REQUEST_TYPE)
        }

        binding.secFilePickerButton.setOnClickListener {
            pickPDFFileTwo.launch(PDF_PCIKER_REQUEST_TYPE)
        }

        binding.mergePdfButton.setOnClickListener {
            Log.i(TAG, "mergePdfButton clicked = ${pdfFileList.size}")
            if (pdfFileList.size == 2) {
                mergeBothPDFFiles(pdfFileList[0], pdfFileList[1])
            } else {
                Toast.makeText(this, "Please select two PDF files to merge.", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun mergeBothPDFFiles(fileOne: File, fileTwo: File) {
        val obj = PDFMergerUtility()
        val mMergedDir: File =
            this.getDir("merged_pdf", Context.MODE_PRIVATE) //Creating an internal dir;
        // this wil create a dir named = app_merged_pdf
        if (!mMergedDir.exists()) {
            mMergedDir.mkdirs()
        }

        Log.e(TAG, "mergeBothPDFFiles: dir = ${mMergedDir.absolutePath}")

        destFileName = "${mMergedDir.absolutePath}/merged_pdf_${getTimestamp()}.pdf"
        obj.destinationFileName = destFileName

        obj.addSource(fileOne)
        obj.addSource(fileTwo)
        try {
            obj.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly())
            Log.i(TAG, "mergeBothPDFFiles: Merged")
            Toast.makeText(this, "Merged Both PDFs successfully.", Toast.LENGTH_LONG).show()
            binding.mergedPathTextView.text = "Merged file path: $destFileName"
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private val pickPDFFileOne =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { it ->
                Log.i(TAG, "pickPDFFileOne_URI = $it")
                val firstPDFFile = getFile(this, it)
                pdfFileList.add(firstPDFFile)
                Log.i(TAG, "pdf_picker_path_first = ${firstPDFFile.absolutePath}")
                binding.pathFirstTextView.text = firstPDFFile.absolutePath
            }
        }

    private val pickPDFFileTwo =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { it ->
                Log.i(TAG, "pickPDFFileTwo_URI = $it")
                val secPDFFile = getFile(this, it)
                pdfFileList.add(secPDFFile)
                Log.i(TAG, "pdf_picker_path_first = ${secPDFFile.absolutePath}")
                binding.pathSecTextView.text = secPDFFile.absolutePath
            }
        }
}



