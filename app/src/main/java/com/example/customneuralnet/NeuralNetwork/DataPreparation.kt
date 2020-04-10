package com.example.customneuralnet.NeuralNetwork

class DataPreparation {

    fun PrepareData():ArrayList<ArrayList<ArrayList<Float>>> {
        
        var data = ArrayList<ArrayList<Float>>()
        var expected = ArrayList<ArrayList<Float>>()
        var allData = ArrayList<ArrayList<ArrayList<Float>>>(2)

        data.add(arrayOf((0.0f), (0.0f), (0.0f)).toCollection(ArrayList<Float>()))
        expected.add(arrayOf((0.0f)).toCollection(ArrayList<Float>()))

        data.add(arrayOf((0.0f), (0.0f), (1.0f)).toCollection(ArrayList<Float>()))
        expected.add(arrayOf((1.0f)).toCollection(ArrayList<Float>()))

        data.add(arrayOf((0.0f), (1.0f), (0.0f)).toCollection(ArrayList<Float>()))
        expected.add(arrayOf((1.0f)).toCollection(ArrayList<Float>()))

        data.add(arrayOf((0.0f), (1.0f), (1.0f)).toCollection(ArrayList<Float>()))
        expected.add(arrayOf((0.0f)).toCollection(ArrayList<Float>()))

        data.add(arrayOf((1.0f), (0.0f), (0.0f)).toCollection(ArrayList<Float>()))
        expected.add(arrayOf((1.0f)).toCollection(ArrayList<Float>()))

        data.add(arrayOf((1.0f), (0.0f), (1.0f)).toCollection(ArrayList<Float>()))
        expected.add(arrayOf((0.0f)).toCollection(ArrayList<Float>()))

        data.add(arrayOf((1.0f), (1.0f), (0.0f)).toCollection(ArrayList<Float>()))
        expected.add(arrayOf((0.0f)).toCollection(ArrayList<Float>()))

        data.add(arrayOf((1.0f), (1.0f), (1.0f)).toCollection(ArrayList<Float>()))
        expected.add(arrayOf((1.0f)).toCollection(ArrayList<Float>()))

        allData.add(data)
        allData.add(expected)

        return Normalize(allData)
    }

    private fun Normalize(values: ArrayList<ArrayList<ArrayList<Float>>>): ArrayList<ArrayList<ArrayList<Float>>>
    {
        var maxExpected = 0.0f
        var minExpected = values[1][0][0]

        for(i in 0 until values[1].size)
        {
            for(j in 0 until values[1][i].size)
            {
                if(maxExpected<values[1][i][j])
                {
                    maxExpected = values[1][i][j]
                }
                if(minExpected>values[1][i][j])
                {
                    minExpected = values[1][i][j]
                }
            }
        }



        for(i in 0 until values[1].size)
        {
            for(j in 0 until values[1][i].size)
            {
                values[1][i][j] = (values[1][i][j] - minExpected)/(maxExpected - minExpected)
            }
        }

        return values
    }


}