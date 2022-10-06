## Code Review

Reviewed by: John Larkin, u6717250

Reviewing code written by: Saquib Khan, u7108513

Component: https://gitlab.cecs.anu.edu.au/u6717250/comp1110-ass2/-/blob/main/src/comp1110/ass2/gui/Game.java#L394-433

### Comments 
#### makeStructures Function
The makeStructures function is responsible for providing the outlines for a structure on
the board. It designs outlines for the roads, settlements, cities and knights.
To do this it iterates through the keys of a HashMap, the values of which
are BuildableStructures with defining coordinates and the type of structure. Depending upon the type of
structure it creates different shape outlines to place on the board. The code makes use of classes such as the BuildableStructures
class and nested classes within the Game class such as the RoadShape class. The HashMap structure follows necessary conventions which
reduces the chance of errors in the code. A suggestion for improvement could be to add more comments to document the code.






