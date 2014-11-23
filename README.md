Machine Learning: 
A Darwinian Approach to Playing Connect Four
=====================
By Aaron Shotkin and Richard Zou
Completed May 2014

Introduction
--------------------
A simple command line connect four game with a neural network AI and genetic algorithms.  
This project was created as our final project for Harvard's CS51: Design and Abstraction.

Please see
[Oour demo video](https://www.youtube.com/watch?v=_qAEaHLIl_M)
for a quick overview of the project.

Overview
--------------------
We implemented a Neural Network AI to play Connect Four that we trained through a Genetic Algorithm. A Neural Network consists of several layers of neurons, each of which sends outputs to every neuron in the next layer. A neuron takes these inputs, and uses some predetermined weights to calculate an output. A Neural Network is essentially a complicated mathematical formula that takes some number of inputs and then outputs some number of outputs based on the weights of its neurons. In a Genetic Algorithm, we start with a random population of Neural Networks, which we also call a Genome. We then assign each of them a fitness by seeing how well the Genome (Neural Network) does against an easy minmax AI. We keep the most successful genomes, and then use them to generate a new generation of Neural Networks that have similar weights to the initial ones.
 
We ended up creating a Neural Network that we can initialize to any size we want. For the sake of playing Connect Four, we have 126 inputs. The reasoning behind this number is that there are 42 squares on the board, and each square has 3 inputs associated with it, representing the 3 possible states of the space (empty, filled by player 1, filled by player 2). We then send it through our hidden layers (we currently are using 2 layers of 126 each, but it’s easy to change) and eventually get 7 outputs where each output corresponds to one of the 7 moves. The highest outputs designates which move the AI will make. Our Genetic Algorithm judges fitness by making our AI play a Min-Max AI in two games, one with the neural net going first and one with the Min-max going first. It then averages the number of turn the AI lasts and return that as fitness (with an added boost for a tie or win).  

As of now, our AI eventually beats the minmax AI on its easy and medium setting.  Not every run of the genetic algorithm converges quickly to a solution, however; so one must be patient with running the genetic algorithm.  Please look at the data folder for sample neural network AI files that you can input into our program that we have evolved through patience. 

Given enough time, we should be able to evolve a neural network AI that can beat the Min-Max algorithm on its highest difficulty setting, but we have not observed this yet because the hard Min-Max AI takes significantly longer to make moves.  
