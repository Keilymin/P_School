package com.burlakov.week1application.activities


import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.burlakov.week1application.Constants
import com.burlakov.week1application.MySpan
import com.burlakov.week1application.R
import com.burlakov.week1application.api.FlickrApi
import com.burlakov.week1application.models.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {


    lateinit var search: Button
    lateinit var searchText: EditText
    lateinit var result: TextView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search = findViewById(R.id.search)
        searchText = findViewById(R.id.editTextName)
        result = findViewById(R.id.result)
        progressBar = findViewById(R.id.progressBar)


        search.setOnClickListener {

            if (searchText.text.toString().replace(" ","") != "") {

                val text = searchText.text.toString()

                searchText.setText("")
                progressBar.visibility = ProgressBar.VISIBLE

                CoroutineScope(Dispatchers.Default).launch {
                    val searchResult = FlickrApi.photoService.search(text)

                    var str = ""
                    for (p: Photo in searchResult.photos.photo) {
                        str += "\n\n${FlickrApi.photoUrl}${p.server}/${p.id}_${p.secret}.jpg\n\n"
                    }

                    val string = SpannableString(str)

                    val matcher = Pattern.compile(
                        Constants.URL_REG_EX,
                        Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
                    ).matcher(str)

                    while (matcher.find()) {
                        val matchStart = matcher.start(1)
                        val matchEnd = matcher.end()
                        string.setSpan(
                            MySpan(str.subSequence(matchStart, matchEnd).toString()),
                            matchStart,
                            matchEnd,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                    }

                    withContext(Dispatchers.Main) {

                        progressBar.visibility = ProgressBar.INVISIBLE

                        result.text = string
                        result.movementMethod = LinkMovementMethod.getInstance()
                    }
                }

            }
        }
    }


}