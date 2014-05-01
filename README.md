# Chef Solo Provisioning
AATAMS is currently built against JDK6 and Grails 1.3.7. If like me you don't have JDK installed on your shiny new iMac and if like me you don't want to install it then you might want to use Vagrant to build and run the app.

## Prerequisites
You need [Vagrant](http://wwww.vagrantup.com) installed, because you are installing Vagrant I will assume that you have, or will have Ruby installed as well. FYI I manage my Ruby through [chruby](https://github.com/postmodern/chruby).

This also assumes that you have an AATAMS database restored on your host machine. Some hints to help do this are included below.

### Bundling
I use [Bundler](http://bundler.io/) to manage my gems for projects, you will at least need Bundler installed into your system gems, I tend to put the latest version of [Rake](http://rake.rubyforge.org/) there too

`$ gem install rake`
`$ gem install bundler`

I like to keep gems and binstubs local to my project bundle as well, to bundle execute
`$ bundle install --path=.bundle/vendor --binstubs=.bundle/bin`

I think convention is to not put them in the `.bundle` folder but I have done that because some of our projects have a `bin` directory and it might confuse people to see the stubs in there and alternatively I don't want to `.gitignore` them all. I may when I learn more about `.gitignore` regexes.

### Provision
Now it should be as simple as `$ vagrant up`

# Restoring the AATAMS database
Moved to [doc/BACKUP_AND_RESTORE.md](doc/BACKUP_AND_RESTORE.md)