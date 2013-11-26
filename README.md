To work with the upset2 codebase, you must install java
and the [play framework SDK](http://downloads.typesafe.com/play/2.2.1/play-2.2.1.zip).

Unzip the SDK to some directory. For instance, I unzip it into
~/app, which creates ~/app/play-2.2.1.

The directory created by extracting the zip file should be in your $PATH, as
so:

    $ export PATH=/home/dylan/app/play-2.2.1:$PATH

git clone the githup repo, and chdir into the working directory.

To run the test suite:

    $ play test

To run an interactive scala console with all the app's code loaded:

    $ play console

To run a local instance of the application:

    $ play run

There are [instructions for setting up an IDE](http://www.playframework.com/documentation/222.1/IDE) on the play framework website.
