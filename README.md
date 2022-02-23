# cloryboard

Cloryboard is a Clojure toolset for creating osu! storyboards. Clojure is the ideal language for creating a storyboard, as it is functional with a great side by side collaborative REPL environment.

The repo is set up with the following structure. There are three main classes of "object" types (really they're functions, since this is clojure, but I digress). There are effects, functions, and events. Technically, we also have "generators", but these are a sub-type of effects specific to image creation (like generating a set of letters for lyrics). Functionally, effects are groupings of images that are created and handled as a stand alone object grouping. These do things like generating lyrics, or a set of particles in a particular alignment. From there, functions manipulate these groups with extensive modularity. Finally, events are ways to organize your storyboard linearly into seperate files to simplify managing your storyboard.

The general structure of all functions and effects are as follows.

Functions:

```clojure
{effect-of-some-kind
  {:edn-map 1
   :that-contains 2
   :all-relevent 3
   :definitions-and 4
   :metadata-stats 5
   :about-the-current 6
   :function-such-as 7
   :scale 0.15
   :lyric-line "Through the fire\nand the flames,"
   :text-align "Centre"
   :etc "etc"}}

(function-of-some-kind
  {:relevent-metadata
   :such-as
   :fade-in-begin
   :move-easing
   :etc "etc"}
   objects)
```

Atleast right now, still working on this...

## Installation

Pull it with git, run lein run to build SB. Right now its configured with my SB project though. To replace, create your own events and update the file path in the main.clj file.

## Usage

Cloryboard is a REPL based toolkit, designed to optimize the flow of developing a storyboard.

## Options

None, though I could add the storyboard file path to this theoretically, but this isn't really needed.
