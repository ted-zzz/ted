ted
===

## Building ####################################################################

### Requirements ###############################################################
 * [Apache Thrift](http://incubator.apache.org/thrift/) 0.2.0
 * [Ant](http://ant.apache.org/)
 * [Maven](http://maven.apache.org/)

### Source #####################################################################
Check-out the sources from GitHub:

    $ git clone git://github.com/ted/ted.git

### Build ######################################################################
Currently to build ted, both ant and maven are required:

    $ cd ted/ted-api
    $ mvn
    $ cd ..
    $ ant
    $ cd ted-ui/gwt-ted
    $ ant

## Running #####################################################################
Ted can be run from eclipse by following these steps:

 1. In Eclipse set a classpath variable `TED_LIB_DIR` to point to the newly
 created `lib` directory in ted.
 2. In Eclipse install [GWT](https://code.google.com/webtoolkit/usingeclipse.html)
 3. Open projects in Eclipse.
 4. Run `ted-server` from inside eclipse running the `nu.ted.Server` class.
This will start the server running on localhost port `9030`. Any client written
using the ted.thrift API should be able to talk to the service on this port.
 5. Run `gwt-ted` from inside eclipse.
   * compile gwt-ted project: right-click the project -> Google -> GWT Compile
   -> Compile
   * run the gwt-ted UI: right-click the project -> RunAs -> Web Application.
   This will run the debug webapp. When we deploy we may include something like
   jetty to run the web ui.

## Lexicon #####################################################################

Or what in the world I'm talking about.

 * [ted-api](http://github.com/ted/ted/tree/master/ted-api): The Thrift based
 API allowing clients in many languages to talk with the ted-server.
 * [ted-server](http://github.com/ted/ted/tree/master/ted-server): The back-end
 daemon that looks for and downloads torrents.
 * [ted-ui](http://github.com/ted/ted/tree/master/ted-ui): The UIs included in
 the Ted project.
 * [gwt-ted](http://github.com/ted/ted/tree/master/ted-ui/gwt-ted): The GWT UI
 to talk to the ted-server.

In Ted there is a back-end domain model. This is the data that will be
persisted between restarts of the ted-server. When possible they will
use the simplest and shortest names for an item:

 * Series
 * Episode

GitHub
=====

##### Following are some details for those of you who would like to contribute to the development of ted.
The ted project is currently hosted in GitHub. If you have a GitHub account and
would like to contribute we recommend you fork this repository. If you just want
the latest source you can clone from: git://github.com/ted/ted.git.

For developers that are using GitHub, this is some information that may make it
easier to submit changes and keep in sync.

First you have to know about remotes. A remote is simply a remote repository.
Where non-distributed VCS use one remote, Git allows you to have any number. So
changes can be pushed and pulled from/to many different locations. To keep it
simple(er) here we're just going to use two. We'll get into them in a minute.

We're going to create a Fork of the main git repository. With Git everyone has
all the source, and anyone can create a fork. So if you decide you can write a
better ted, do so, and people may start forking your repository instead of the
ted/ted repository. To fork the main repository log in to GitHub and browse to
http://github.com/ted/ted. Click the 'Fork' button. After a slight delay you'll
end up with your own <username>/ted fork.

This <username>/ted fork is yours. You can do whatever you like in there.
General Git etiquette says that you shouldn't rewrite history that you've
published, but it's your repository so you don't have to follow any etiquette.
It's unlikely anyone will fork your repository when they could easily fork
ted/ted, and if they do then the rewritten history is their problem.

Next we'll clone the <username>/ted repository on to your local machine. That
repository will be called <local>/ted for the remainder of this document. It's
worth noting that the <local>/ted repository is another full clone of the source
code. Any of these three repositories contains all the history of the project,
and can survive the deletion of the others. To create the local clone run:

    $ git clone git@github.com:<username>/ted.git

After creating a local copy of the source Git will setup the first remote. This
remote will be called 'origin' and will point back to <username>/ted on GitHub.
By default Git will use this remote when you don't specify one.

To make keeping up to date with ted/ted and changes from other developers we'll
next add a remote that points back to that repository. To do this run:

    $ git remote add upstream git://github.com/ted/ted.git
    $ git fetch upstream

Now there are a lot of ways you could go from here depending on your Git
experience. We'll explain one workflow, but feel free follow your own. As long
as we get a patch from you, we don't care how your repository looks.

Branch-per-feature:

For this workflow we're going to keep <username>/master always tracking
ted/master, and never commit any changes to it. As soon as you start committing
changes to <username>/master you have to start dealing with merging ted/master
changes in, and that can get confusing. So to get <local>/master up-to-date with
ted/master, and then push that update to <username>/ted, every so often do the
following:

    Update Master:
    $ git checkout master
    $ git pull upstream master
    $ git push origin master

To write a feature (or fix a bug), do the following:

    Doing Work:
    $ <do 'Update Master'>
    $ git checkout -b <branchname>
     Loop:
       $ <work>
       $ git add <changed file>
       $ git add <changed file>
       $ git commit
    $ git push origin <branchname>

Do the work loop as many times as makes sense. You can also amend patches, or
squash some together, or whatever else. When ready to publish that branch to the
outside work the last command will push it to <username>/ted/<branchname>. You
can continue pushing to this branch as often as you like, and when it's ready to
be include in ted send simply send a pull request from the GitHub UI.

When the pull request is accepted and your patches included in the ted/ted
repository you may delete your local branch. The commits in your local branch
will be slightly different from the commits added to the ted/ted repository
because the ted/ted branches were committed at a different time (likely by a
different person). Don't worry, you'll still be listed as the author on any
patches you send. To delete the branch at <username>/ted/<branchname> do the
following:

    $ git push origin :<branchname>

While working in this branch you find changes made to ted/ted/master that you
would like to include in your branch. There are a couple ways to do this. Here
we will show the way to do it rewriting history. This is not what you'd normally
want to do on a published branch because other people could have commits that
extend that branch. Do not do this if you've sent a 'Pull Request', or at least
send a message asking us to abort the pull.

    Rewrite history to move changes ahead:
    $ <do 'Update Master'>
    $ git checkout <branchanme>
    $ git rebase master
       -- may have to fix commits here if there's conflicts
    $ git push -f origin <branchname>

The reason we're showing a rewrite of history instead of doing a merge is
because if you find a conflict in the rebase you can fix that one commit and
keep a linear history, whereas if you find a conflict in the merge the fix will
instead be included in the merge commit. The '-f' to the push will force this
push to happen, even though it rewrites history.

Either way, moving a branch will hopefully be a rare occurrence. If kept small
we should be able to pull in these branches and have them disappear quite often.

If you have any Git or GitHub questions please send me ([KenMacD](http://github.com/inbox/new/KenMacD)) a message on GitHub directly.
