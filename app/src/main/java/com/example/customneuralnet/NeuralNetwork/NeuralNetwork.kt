package com.example.customneuralnet.NeuralNetwork

import kotlin.math.exp
import kotlin.random.Random

class NeuralNetwork {


    var layer = ArrayList<Int>()
    var layers = ArrayList<Layer>()
    var learningRate = 0.0f
    var options = ""
    constructor(layer: ArrayList<Int>, learningRate: Float, options: String){
        this.layer = ArrayList(layer.size)
        this.learningRate = learningRate
        this.options = options
        for (i in 0 until layer.size)
        {
            this.layer.add(layer[i])
        }

        layers = ArrayList(layer.size-1)
        for(i in 0 until layer.size-1)
        {
            layers.add(Layer(layer[i],layer[i+1], learningRate, options))
        }
    }



    fun FeedForward(inputs: ArrayList<Float>):ArrayList<Float>
    {
        layers[0].FeedForward(inputs)
        for(i in 1 until layers.size)
        {
            layers[i].FeedForward(layers[i-1].outputs)
        }
        return layers[layers.size-1].outputs.toCollection(ArrayList())
    }

    fun BackProp(expected: ArrayList<Float>)
    {
        for(i in layers.size-1 downTo 0)
        {
            if(i == layers.size - 1)
            {
                layers[i].BackPropOutput(expected)
            }
            else
            {
                layers[i].BackPropHidden(layers[i+1].gamma, layers[i+1].weights)
            }
        }

        for(i in 0 until layers.size)
        {
            layers[i].UpdateWeights()
        }
    }

    class Layer {
        var numberOfInputs = 0
        var numberOfOutputs = 0

        var outputs = ArrayList<Float>()
        var inputs = ArrayList<Float>()
        var gamma = ArrayList<Float>()
        var error = ArrayList<Float>()
        var weights = ArrayList<ArrayList<Float>>()
        var weightsDelta = ArrayList<ArrayList<Float>>()
        var random = Random
        var learningRate = 0.0f
        var options = ""


        constructor(numberOfInputs: Int, numberOfOutputs: Int, learningRate: Float, options: String)
        {
            this.numberOfInputs = numberOfInputs
            this.numberOfOutputs = numberOfOutputs
            this.learningRate = learningRate
            this.options = options

            inputs = ArrayList<Float>(numberOfInputs)
            for(i in 0 until numberOfInputs)
                inputs.add(0.0f)
            outputs = ArrayList<Float>(numberOfOutputs)
            for(i in 0 until numberOfOutputs)
                outputs.add(0.0f)

            for(x in 0 until numberOfOutputs) {
                var array = ArrayList<Float>()
                for(y in 0 until numberOfInputs)
                {
                    array.add(0.0f)
                }
                weights.add(array)
            }
            for(x in 0 until numberOfOutputs) {
                var array = ArrayList<Float>()
                for(y in 0 until numberOfInputs)
                {
                    array.add(0.0f)
                }
                weightsDelta.add(array)
            }

            gamma = ArrayList<Float>(numberOfOutputs)
            error = ArrayList<Float>(numberOfOutputs)
            for(i in 0 until numberOfOutputs)
            {
                gamma.add(0.0f)
                error.add(0.0f)
            }

            InitializeWeights()
        }

        fun InitializeWeights()
        {
            for(i in 0 until numberOfOutputs)
            {
                for(j in 0 until numberOfInputs)
                {
                    weights[i][j] = random.nextFloat() - 0.5f
                }
            }
        }

        fun FeedForward(inputs: ArrayList<Float>): ArrayList<Float>
        {
            this.inputs = inputs

            for(i in 0 until numberOfOutputs)
            {
                outputs[i] = 0.0f
                for(j in 0 until numberOfInputs)
                {
                    outputs[i] += inputs[j]*weights[i][j]
                }
                if(options == "TanH")
                    outputs[i] = Math.tanh(outputs[i].toDouble()).toFloat()
                else if(options == "ReLU")
                    outputs[i] = Relu(outputs[i])
                else if(options == "Sigmoid")
                    outputs[i] = Sigmoid(outputs[i])
                else if(options == "Identity")
                    outputs[i] = Identity(outputs[i])
                else if(options == "Hard Sigmoid")
                    outputs[i] = HardSigmoid(outputs[i])
                else if(options == "Cube")
                    outputs[i] = Cube(outputs[i])
                else if(options == "Swish")
                    outputs[i] = Swish(outputs[i])
                else if(options == "Soft Plus")
                    outputs[i] = SoftPlus(outputs[i])
                else
                    outputs[i] = 9999999999f

            }
            return outputs
        }

        fun Relu(value: Float):Float
        {
            if(value >=0)
            {
                return value
            }
            else
                return 0f
        }
        fun ReluDer(value: Float):Float
        {
            if(value >0)
            {
                return value
            }
            else
                return 0f
        }

        fun Sigmoid(value: Float):Float
        {
            return 1/(1+Math.exp(-value.toDouble())).toFloat()
        }
        fun SigmoidDer(value: Float):Float
        {
                return (Math.exp(-value.toDouble())/(Math.pow((exp(-value)+1).toDouble(), 2.toDouble()))).toFloat()
        }

        fun Identity(value: Float):Float
        {
            return value
        }
        fun IdentityDer(value: Float):Float
        {
            return 1f
        }

        fun HardSigmoid(value: Float):Float
        {
            var x = 0.2*value+0.5
            if(value > 0 && value <= x)
            {
                if(x >= 1)
                {
                    return 1f
                }
                else
                {
                    return x.toFloat()
                }
            }
            else
                return 0f
        }
        fun HardSigmoidDer(value: Float):Float
        {
            if(value > -(5/2) && value < (5/2))
            {
                return 0.2f
            }
            else
                return 0f
        }

        fun Cube(value:Float):Float
        {
            return value*value*value.toFloat()
        }
        fun CubeDer(value:Float):Float
        {
            return 3*value*value.toFloat()
        }

        fun Swish(value:Float):Float
        {
            return value*Sigmoid(value)
        }
        fun SwishDer(value:Float):Float
        {
            return ((Math.exp(value.toDouble())*(value + Math.exp(value.toDouble()) + 1))/
                    (Math.pow(Math.exp(value.toDouble())+1, 2.toDouble()))).toFloat()
        }

        fun SoftPlus(value:Float):Float
        {
            return Math.log(1+Math.pow(Math.E, value.toDouble())).toFloat()
        }

        fun SoftPlusDer(value:Float):Float
        {
            return ((Math.exp(value.toDouble()))/(1 + Math.exp(value.toDouble()))).toFloat()
        }

        fun TanhDer(value: Float):Float
        {
            return 1 - (value*value)
        }

        fun BackPropOutput(expected: ArrayList<Float>)
        {
            for(i in 0 until numberOfOutputs)
            {
                error[i] = outputs[i] - expected[i]
                if(options == "TanH")
                    gamma[i] = error[i] * TanhDer(outputs[i])
                else if(options == "ReLU")
                    gamma[i] = error[i] * ReluDer(outputs[i])
                else if(options == "Sigmoid")
                    gamma[i] = error[i] * SigmoidDer(outputs[i])
                else if(options == "Identity")
                    gamma[i] = error[i] * IdentityDer(outputs[i])
                else if(options == "Hard Sigmoid")
                    gamma[i] = error[i] * HardSigmoidDer(outputs[i])
                else if(options == "Cube")
                    gamma[i] = error[i] * CubeDer(outputs[i])
                else if(options == "Swish")
                    gamma[i] = error[i] * SwishDer(outputs[i])
                else if(options == "Soft Plus")
                    gamma[i] = error[i] * SoftPlusDer(outputs[i])
                else
                    gamma[i] = 999999999f
                for(j in 0 until numberOfInputs)
                {
                    weightsDelta[i][j] = gamma[i] * inputs[j]
                }
            }
        }

        fun BackPropHidden(gammaForward: ArrayList<Float>, weightsForward: ArrayList<ArrayList<Float>>)
        {
            for(i in 0 until numberOfOutputs)
            {
                gamma[i] = 0.0f
                for(j in 0 until gammaForward.size)
                {
                    gamma[i] += gammaForward[j] * weightsForward[j][i]
                }
                if(options == "TanH")
                    gamma[i] *= TanhDer(outputs[i])
                else if(options == "ReLU")
                    gamma[i] *= ReluDer(outputs[i])
                else if(options == "Sigmoid")
                    gamma[i] *= SigmoidDer(outputs[i])
                else if(options == "Identity")
                    gamma[i] *= IdentityDer(outputs[i])
                else if(options == "Hard Sigmoid")
                    gamma[i] *= HardSigmoidDer(outputs[i])
                else if(options == "Cube")
                    gamma[i] *= CubeDer(outputs[i])
                else if(options == "Swish")
                    gamma[i] *= SwishDer(outputs[i])
                else if(options == "Soft Plus")
                    gamma[i] *= SoftPlusDer(outputs[i])
                else
                    gamma[i] = 999999999f
            }
            for(i in 0 until numberOfOutputs)
            {
                for(j in 0 until numberOfInputs)
                {
                    weightsDelta[i][j] = gamma[i] * inputs[j]
                }
            }
        }

        fun UpdateWeights()
        {
            for(i in 0 until numberOfOutputs)
            {
                for(j in 0 until numberOfInputs)
                {
                    weights[i][j] -= weightsDelta[i][j]*learningRate
                }
            }
        }
    }
}