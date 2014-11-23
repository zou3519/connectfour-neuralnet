Machine Learning: 
A Darwinian Approach to Playing Connect Four
=====================
By Aaron Shotkin and Richard Zou

Initial project completed May 2014, future work possible

Introduction
--------------------
A simple command line connect four game with a neural network AI and genetic algorithms.  
This project was created as our final project for Harvard's CS51: Design and Abstraction.

Please see
[our demo video](https://www.youtube.com/watch?v=_qAEaHLIl_M)
for a quick overview of the project.

The project was originally coded on command line with a Makefile, but we have ported it to IntelliJ to make it more
cross-platform.

Feature List and Background
---------------------

###Neural Network
A neural network is a data structure that emulates the structure of the human brain.  It takes in a layer of input neurons, has hidden layers of neurons, and an output layer of neurons.  The input neurons “fire” synapses to the hidden layer, which in turn sends signals to more hidden layers, and finally produces an output.  These signals are controlled by weights between two neurons.  The combination of these weights and the structure of the neural network produces an algorithm where some input is transformed into some desired product.

###Genetic Algorithm
A genetic algorithm is a method of training an AI that models the evolutionary notion of natural selection. In this type of algorithm, the program randomly creates several AI, each of which are composed of several genes. The AIs are then tested for fitness, and the most fit ones are kept. These are used to produce the next generation of AI. This process is then iterated for many generation until some ending condition is met (whether it be a fitness guideline or a certain number of generations pass).
	

###Application to a Connect Four game AI:
In the case of Connect Four, our program will start with 126 (42 *3) nodes where each board position has three nodes. Depending on the state of the position, one of three nodes first. Theses signals then pass through several hidden layer, and it results in 7 output, one for each possible move. The strongest output is chosen. 

These neural networks will then be trained using a genetic algorithm where the weights for each node will be the genes. In order to measure fitness, all the programs in the same generation will play each other and a couple of min-max AIs. Depending on their results, certain AIs will be kept in order to teach the next generation.


What we did
--------------------
We implemented a Neural Network AI to play Connect Four that we trained through a Genetic Algorithm. A Neural Network consists of several layers of neurons, each of which sends outputs to every neuron in the next layer. A neuron takes these inputs, and uses some predetermined weights to calculate an output. A Neural Network is essentially a complicated mathematical formula that takes some number of inputs and then outputs some number of outputs based on the weights of its neurons. In a Genetic Algorithm, we start with a random population of Neural Networks, which we also call a Genome. We then assign each of them a fitness by seeing how well the Genome (Neural Network) does against an easy minmax AI. We keep the most successful genomes, and then use them to generate a new generation of Neural Networks that have similar weights to the initial ones.
 
We ended up creating a Neural Network that we can initialize to any size we want. For the sake of playing Connect Four, we have 126 inputs. The reasoning behind this number is that there are 42 squares on the board, and each square has 3 inputs associated with it, representing the 3 possible states of the space (empty, filled by player 1, filled by player 2). We then send it through our hidden layers (we currently are using 2 layers of 126 each, but it’s easy to change) and eventually get 7 outputs where each output corresponds to one of the 7 moves. The highest outputs designates which move the AI will make. Our Genetic Algorithm judges fitness by making our AI play a Min-Max AI in two games, one with the neural net going first and one with the Min-max going first. It then averages the number of turn the AI lasts and return that as fitness (with an added boost for a tie or win).  

As of now, our AI eventually beats the minmax AI on its easy and medium setting.  Not every run of the genetic algorithm converges quickly to a solution, however; so one must be patient with running the genetic algorithm.  Please look at the data folder for sample neural network AI files that you can input into our program that we have evolved through patience. 

Given enough time, we should be able to evolve a neural network AI that can beat the Min-Max algorithm on its highest difficulty setting, but we have not observed this yet because the hard Min-Max AI takes significantly longer to make moves.  


Technical Specifications
--------------------------


###Neural Network Classes: Package NeuralNet

####Neuron
Will record the number of inputs into the neuron and an array of weights for each of the inputs.

####NeuronLayer
Represents a layer of neurons.  Will contain a list of neurons.

####NeuralNet
Represents the entire network.  Will store each layer of neurons.  Will
also be able to replace all of the weights between the connections of neurons.  Finally, will also be able to compute the output given a set of inputs to the network.

###Genetic Algorithm Classes: Package Genetic

####Genome
Represents the genes of an AI. Will contain the methods for two genomes to interact and produce a new genome.

####GenAlg
Will create new Genomes and then pick the most fit ones. Will create a new generation based on the most fit ones in the previous generation and will iterate as long as the user wants. 

###Connect four classes: Package Connect

####Board
Represents the Connect Four board
	
####Game
Controls the game sequence between 2 players, AI or human
####Move
Represents a “move” in the connect four game.  A move
is a (player, position) pair.
####Player
The abstract class for Human and NeuralNetAI
####Human
A class for human interaction with the game
####NeuralNetAI
The AI based on the neural network
####ConnectGenome
The Connect Four genome.  Extends Genetic.Genome.
