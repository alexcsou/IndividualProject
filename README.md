<h1 align="center">𝗙𝗜𝗫𝗔𝗧𝗘𝗗 - Reflection Dashboard</h1>


<p align="center">
  <a href="#dart-description">Description</a> &#xa0; | &#xa0; 
  <a href="#sparkles-functionality-overview">Functionality</a> &#xa0; | &#xa0;
  <a href="#rocket-technologies">Technologies</a> &#xa0; | &#xa0;
  <a href="#white_check_mark-getting-started">Getting Started</a> &#xa0; | &#xa0;
  <a href="https://github.com/alexcsou" target="_blank">Author</a>
</p>

<br>

## :dart: Description ##

This project was developed as part of my final year project (module 6CCS3PRJ) at King's College London during the year 2021-2022. The objective of this project is to allow individuals working remotely in a corporate context to better adapt and benefit from remote ways of working, namely online meetings or videoconferences. To this end, this repo contains code that creates a JavaFX application which allows meeting participants to reflect, analyse and adress their behaviour during a specific online meeting via a collection of charts and other data visualisations. All the data is collected and analysed from a pair of user-provided meeting transcripts (generated when a meeting is recorded on Microsoft Teams). This application is part of a wider Human-Computer Interaction project, and is the result of extensive design thinking, of which the majority can be found on <a href="https://miro.com/app/board/o9J_lhS0auk=/?invite_link_id=761840512232" target="_blank">here, on Figma</a>.

## :sparkles: Functionality Overview ##

This Application is structured into three main sections, which are accessed after a user successfully provides the required transcript files. The first section is the heart of the app, containing large amount of charts displaying data about each user and their behaviour during the meeting. Share of spoken time, sentences, words are all tracked. Sentence sentiment is analysed through the use of StandfordNLP. Hesitations, filler words and sentence types are also tracked metrics. The second section provides detailed insights to each user regarding how they can alter and improve (if possible) their performance. The last tab features the meeting transcript, but structured a "chat bubbles" allowing a user to more clearly read and scroll through it. 

## :rocket: Technologies ##

The following tools were used in this project:

- [JavaFX](https://openjfx.io/)
- [StandfordNLP](https://nlp.stanford.edu/software/)
- [Maven](https://maven.apache.org/)

## :white_check_mark: Getting Started ##

Before starting :checkered_flag:, you need to have [Java](https://www.oracle.com/java/technologies/downloads/), [Maven](https://maven.apache.org/) and [Git](https://git-scm.com) installed.

```console
# Clone this project
$ git clone https://github.com/alexcsou/IndividualProject.git

# Open the project in any IDE. To run the code, launch the 'Main' class. To find the main class when in the downloaded project directory:

$ cd project
$ cd src
$ cd main
$ cd java
$ cd com
$ cd soloproject
```
Launching the Main class will start the application. This may take a while as the StandfordNLP model needs to be loaded for sentimental analysis to proceed. An executable JAR of the project is readily available for download <a href="https://1drv.ms/u/s!Ajxfgyd9ge-Sh7JrjB-wDjWtpHXqcQ" target="_blank">here, should anything go wrong</a>.


Made by <a href="https://github.com/alexcsou" target="_blank">Alexandre Chouman</a>.

&#xa0;

<a href="#top">Back to top</a>
