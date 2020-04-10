package com.example.customneuralnet

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.daimajia.numberprogressbar.NumberProgressBar
import com.example.customneuralnet.NeuralNetwork.NeuralNetwork
import kotlinx.android.synthetic.main.add_data_dialog.view.*
import kotlinx.android.synthetic.main.options_dialog.view.*
import kotlinx.android.synthetic.main.progressbar_dialog.view.*


class MainActivity : AppCompatActivity() {

    //var progress: NumberProgressBar? = null
    var mConstraintLayout: ConstraintLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonStart = findViewById<TextView>(R.id.button)
        var epochs = findViewById<EditText>(R.id.cycles)
        val firstHiddenLayer = findViewById<EditText>(R.id.firstHiddenLayer)
        val learningRate = findViewById<EditText>(R.id.learningRate)
        val outputTextView = findViewById<TextView>(R.id.outputTW)
        val inputEditText = findViewById<EditText>(R.id.inputEdt)
        var secondLayerCHB = findViewById<CheckBox>(R.id.checkBox)
        var secondHiddenLayer = findViewById<EditText>(R.id.secondHiddenLayer)
        var secondLayerTW = findViewById<TextView>(R.id.secondHiddenTW)
        var addData = findViewById<Button>(R.id.addData)
        var options = findViewById<Button>(R.id.options)
        var activation = "TanH"

        val mConstraintSet = ConstraintSet()

        var dataFromEDT = ArrayList<ArrayList<ArrayList<Float>>>(2)



        val dialogView = LayoutInflater.from(this).inflate(R.layout.add_data_dialog, null)
        val dialogViewOptions = LayoutInflater.from(this).inflate(R.layout.options_dialog, null)

        var twExpected = dialogView.tw_expected
        var twInput = dialogView.tw_input


        mConstraintLayout = findViewById(R.id.constraintLayout)
        outputTextView.setMovementMethod(ScrollingMovementMethod())

        //progress = findViewById(R.id.progress)

        secondLayerCHB!!.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean ->

            if (b) {


                secondHiddenLayer!!.focusable = View.FOCUSABLE
                secondHiddenLayer!!.visibility = View.VISIBLE
                secondHiddenLayer!!.isFocusableInTouchMode = true
                secondLayerTW!!.visibility = View.VISIBLE
                mConstraintSet.clone(mConstraintLayout)
                mConstraintSet.connect(
                    R.id.cycles, ConstraintSet.TOP,
                    R.id.secondHiddenLayer, ConstraintSet.BOTTOM
                )
                mConstraintSet.applyTo(mConstraintLayout)
            } else {
                secondHiddenLayer!!.focusable = View.NOT_FOCUSABLE
                secondHiddenLayer!!.isFocusableInTouchMode = false
                secondHiddenLayer!!.visibility = View.INVISIBLE
                secondLayerTW!!.visibility = View.INVISIBLE
                mConstraintSet.clone(mConstraintLayout)
                mConstraintSet.connect(
                    R.id.cycles, ConstraintSet.TOP,
                    R.id.checkBox, ConstraintSet.BOTTOM
                )
                mConstraintSet.applyTo(mConstraintLayout)
            }
        }

        options.setOnClickListener{
            val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogViewOptions)
            val mAlertDialog = mBuilder.show()
            var activationSpinner = dialogViewOptions.activationSpinner
            val arrayForSpinner = arrayOf("TanH", "ReLU", "Sigmoid", "Identity", "Hard Sigmoid", "Cube", "Swish", "Soft Plus")
            var arrayAdaper = ArrayAdapter(this,android.R.layout.simple_list_item_1, arrayForSpinner)
            arrayAdaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            activationSpinner.adapter = arrayAdaper

            if (activation != null) {
                var spinnerPosition: Int = arrayAdaper.getPosition(activation)
                activationSpinner.setSelection(spinnerPosition)
            }

            activationSpinner.setOnItemSelectedListener(object:AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent:AdapterView<*>, view:View, position:Int, id:Long) {
                    if (parent.getItemAtPosition(position).equals("TanH"))
                    {}
                    else
                    {
                        val item = parent.getItemAtPosition(position).toString()
                        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show()
                        activation = item
                    }
                }
                override fun onNothingSelected(parent:AdapterView<*>) {}
            })

            dialogViewOptions.buttonBackOptions.setOnClickListener{
                mAlertDialog.dismiss()
                (dialogViewOptions.getParent() as ViewGroup).removeView(dialogViewOptions)
            }
            dialogViewOptions.buttonSubmitOptions.setOnClickListener{
                activation = dialogViewOptions.activationSpinner.selectedItem.toString()
                mAlertDialog.dismiss()
                (dialogViewOptions.getParent() as ViewGroup).removeView(dialogViewOptions)
            }

            mAlertDialog.setOnCancelListener{
                mAlertDialog.dismiss()
                (dialogViewOptions.getParent() as ViewGroup).removeView(dialogViewOptions)
            }
        }

        addData.setOnClickListener{

            val mBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
            val mAlertDialog = mBuilder.show()
            dialogView.dataAdd.setOnClickListener{
                dialogView.inputDataEditText.setText("0,0,0\n0,0,1\n0,1,0\n0,1,1\n1,0,0\n1,0,1\n1,1,0\n1,1,1")
                dialogView.expectedDataEditText.setText("0\n1\n1\n0\n1\n0\n0\n1")
            }
            dialogView.dataCancel.setOnClickListener{
                mAlertDialog.dismiss()
                (dialogView.getParent() as ViewGroup).removeView(dialogView)
            }

            mAlertDialog.setOnCancelListener{
                mAlertDialog.dismiss()
                (dialogView.getParent() as ViewGroup).removeView(dialogView)
            }

            dialogView.inputDataEditText.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    var lines = dialogView.inputDataEditText.lineCount
                    var lineText = ""
                    for(i in 1..lines)
                    {
                        lineText = lineText + i + "\n"
                    }
                    twInput.setText(lineText)
                    twExpected.setText(lineText)

                }

            })

            dialogView.expectedDataEditText.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    var lines = dialogView.expectedDataEditText.lineCount
                    var lineText = ""
                    for(i in 1..lines)
                    {
                        lineText = lineText + i + "\n"
                    }
                    twExpected.setText(lineText)
                    twInput.setText(lineText)
                }

            })

            dialogView.inputDataEditText.setOnKeyListener{ view: View, i: Int, keyEvent: KeyEvent ->
                if((keyEvent.action === KeyEvent.ACTION_DOWN)&&
                    (i == KeyEvent.KEYCODE_ENTER))
                {
                    dialogView.inputDataEditText.text.insert(dialogView.inputDataEditText.selectionEnd,"\n")
                    dialogView.inputDataEditText.setSelection(dialogView.inputDataEditText.selectionEnd)
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            dialogView.expectedDataEditText.setOnKeyListener{ view: View, i: Int, keyEvent: KeyEvent ->
                if((keyEvent.action === KeyEvent.ACTION_DOWN)&&
                    (i == KeyEvent.KEYCODE_ENTER))
                {
                    dialogView.expectedDataEditText.text.insert(dialogView.expectedDataEditText.selectionEnd,"\n")
                    dialogView.expectedDataEditText.setSelection(dialogView.expectedDataEditText.selectionEnd)
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            dialogView.dataSubmit.setOnClickListener{

                if(dialogView.inputDataEditText.text.toString().isNotEmpty()) {
                    var substring = ""

                    var lines = countChar(dialogView.inputDataEditText.text.toString(), '\n')
                    var row = ArrayList<Float>()
                    var input = ArrayList<ArrayList<Float>>()
                    var expected = ArrayList<ArrayList<Float>>()
                    for (i in 0 until dialogView.inputDataEditText.text.length) {
                        if (dialogView.inputDataEditText.text.toString()[i].isDigit() ||
                            dialogView.inputDataEditText.text.toString()[i] == '.') {
                            substring += dialogView.inputDataEditText.text.toString()[i]
                        }
                        if (dialogView.inputDataEditText.text.toString()[i] == ',') {
                            row.add(substring.toFloat())
                            substring = ""
                        }
                        if (dialogView.inputDataEditText.text.toString()[i] == '\n') {
                            row.add(substring.toFloat())
                            input.add(row)
                            substring = ""
                            row = ArrayList<Float>()
                        }
                        if (i == dialogView.inputDataEditText.text.length - 1) {
                            row.add(substring.toFloat())
                            input.add(row)
                            substring = ""
                            row = ArrayList<Float>()
                        }
                    }
                    dataFromEDT.add(input)


                    row = ArrayList<Float>()
                    for (i in 0 until dialogView.expectedDataEditText.text.length) {
                        if (dialogView.expectedDataEditText.text.toString()[i].isDigit() ||
                            dialogView.expectedDataEditText.text.toString()[i] == '.') {
                            substring += dialogView.expectedDataEditText.text.toString()[i]
                        }
                        if (dialogView.expectedDataEditText.text.toString()[i] == ',') {
                            row.add(substring.toFloat())
                            substring = ""
                        }
                        if (dialogView.expectedDataEditText.text.toString()[i] == '\n') {
                            row.add(substring.toFloat())
                            expected.add(row)
                            substring = ""
                            row = ArrayList<Float>()
                        }
                        if (i == dialogView.expectedDataEditText.text.length - 1) {
                            row.add(substring.toFloat())
                            expected.add(row)
                            substring = ""
                            row = ArrayList<Float>()
                        }
                    }
                    dataFromEDT.add(expected)
                    mAlertDialog.dismiss()
                    (dialogView.getParent() as ViewGroup).removeView(dialogView)
                }
                else
                    Toast.makeText(this,"Please fill all data!",Toast.LENGTH_SHORT).show()
            }
        }




        buttonStart.setOnClickListener {

            if (epochs.text.toString().isNotEmpty() &&
                firstHiddenLayer.text.toString().isNotEmpty() &&
                learningRate.text.toString().isNotEmpty() &&
                (!secondLayerCHB.isChecked || (secondLayerCHB.isChecked && secondHiddenLayer.text.toString().isNotEmpty()))
            ) {

                var data = dataFromEDT

                if (CheckData(data, outputTextView)) {


                    var maxNumber = epochs.text.toString().toInt()


                    var netParameters = arrayListOf<Int>()
                    if (secondLayerCHB.isChecked) {

                        netParameters = arrayListOf<Int>(
                            data[0][0].size,
                            firstHiddenLayer.text.toString().toInt(),
                            secondHiddenLayer.text.toString().toInt(),
                            data[1][0].size
                        )

                    } else {
                        netParameters = arrayListOf<Int>(
                            data[0][0].size,
                            firstHiddenLayer.text.toString().toInt(),
                            data[1][0].size
                        )
                    }

                    var net = NeuralNetwork(netParameters, learningRate.text.toString().toFloat(), activation)
                    ProgressTask(
                        net,
                        /*progress!!,*/
                        maxNumber,
                        this,
                        inputEditText,
                        outputTextView,
                        data,
                        mConstraintLayout!!
                    ).execute()
                }
            } else
                Toast.makeText(this, "Please fill all data!", Toast.LENGTH_SHORT).show()

        }
    }


    fun CheckData(inputData: ArrayList<ArrayList<ArrayList<Float>>>, textView: TextView):Boolean
    {
        var checker = false
        for(i in 0 until inputData.size)
        {
            for (j in 0 until inputData[i].size) {
                if(i==0) {
                    if (inputData[i][j].size != inputData[0][0].size) {
                        textView.setText("Data error")
                        return false
                    } else {
                        checker = true
                    }
                }
                else if(i==1)
                {
                    if (inputData[i][j].size != inputData[1][0].size) {
                        textView.setText("Data error")
                        return false
                    } else {
                        checker = true
                    }
                }
            }
        }
        return checker
    }



    internal class ProgressTask (var net: NeuralNetwork, var max: Int, var context: Activity, var inputEditText:EditText, var outputTextView: TextView, var allData: ArrayList<ArrayList<ArrayList<Float>>>, var layout: ConstraintLayout): AsyncTask <Void, Int, Int>()
    {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.progressbar_dialog, null)
        val mBuilder = android.app.AlertDialog.Builder(context).setView(dialogView).setCancelable(false)
        val mAlertDialog = mBuilder.show()
        var progress: NumberProgressBar = dialogView.progress


        override fun onPreExecute() {
            super.onPreExecute()
            progress.visibility = View.VISIBLE
            progress.max = max
            mAlertDialog.show()



        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)

            progress.setProgress(values[0]!!)
        }

        override fun doInBackground(vararg params: Void?): Int? {
            var i = 0

            while (i<max) {

                for(j in 0 until allData[0].size)
                {
                    net.FeedForward(allData[0][j])
                    net.BackProp(allData[1][j])
                }
                publishProgress(i)
                i++
            }


            return null
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            var out = StringBuilder()

            progress.visibility = View.INVISIBLE
            Toast.makeText(context, "Learning finished!", Toast.LENGTH_SHORT).show()

            var inputData = ArrayList<ArrayList<Float>>()

            var inputDataText = inputEditText.text.toString()
            var substring = ""
            var row = ArrayList<Float>()
            for (i in 0 until inputDataText.length)
            {
                if(inputDataText[i].isDigit() || inputDataText[i] == '.')
                {
                    substring += inputDataText[i]
                }
                else if(inputDataText[i] == ',')
                {
                    row.add(substring.toFloat())
                    substring = ""
                }
                else if(inputDataText[i] == '\n')
                {
                    if(substring.isNotEmpty()) {
                        row.add(substring.toFloat())
                        inputData.add(row)
                        substring = ""
                        row = ArrayList<Float>()
                    }
                }
                if(i == inputDataText.length-1)
                {
                    if(substring.isNotEmpty()) {
                        row.add(substring.toFloat())
                        inputData.add(row)
                        substring = ""
                        row = ArrayList<Float>()
                    }
                }
            }

            for(i in 0 until inputData.size)
            {
                out.appendln(
                    Denormalize(allData, net.FeedForward(
                        inputData[i].toCollection(ArrayList<Float>())
                    )[0]).toString()
                )
            }

            outputTextView.setMovementMethod(ScrollingMovementMethod())
            outputTextView.text = out
            layout.isClickable = true
            mAlertDialog.dismiss()
        }


        fun Denormalize(data: ArrayList<ArrayList<ArrayList<Float>>>, value: Float): Float
        {

            var max = 0.0f
            var min = data[1][0][0]


            for(i in 0 until data[1].size)
            {
                for (j in 0 until data[1][i].size)
                {
                    if(max<data[1][i][j])
                    {
                        max = data[1][i][j]
                    }
                    if(min>data[1][i][j])
                    {
                        min = data[1][i][j]
                    }
                }
            }


            return value*(max-min)+min
        }
    }

    fun countChar(str: String, c: Char): Int {
        var count = 0
        for (i in 0 until str.length) {
            if (str[i] == c) count++
        }
        return count
    }
}
