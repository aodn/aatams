# Restoring the AATAMS database
This assumes that you have already obtained a backup of the AATAMS database.

First thing you will want to do is tweak some postgres settings to help it load faster. The values listed here are used for a 4 core 8GB iMac so tweak your settings accordingly, check your `server.log` when you start postgres to see if there are any issues as YMMV. [Tuning Your PostgreSQL Server](http://wiki.postgresql.org/wiki/Tuning_Your_PostgreSQL_Server)

In `postgresql.conf`

```
shared_buffers = 2GB
work_mem = 100MB
maintenance_work_mem = 512MB
synchronous_commit = off
effective_cache_size = 4GB
```

You will need to add the IP that the app on the Vagrant VM will try to connect via to the listen addresses
`listen_addresses = 'localhost,131.217.38.18`

In `pg_hba.conf` I allow connections from any IP
`host    all             all             0.0.0.0/0               md5`

You may want to be more strict than that.

Before you can restart postgres if you are on a POSIX machine you may need to tweak some shared memory kernel settings

```
sudo sysctl -w kern.sysv.shmmax=2200158208
sudo sysctl -w kern.sysv.shmall=2200158208
```

This will apply the settings immediately if you want to keep at restart then you need to edit your `sysctl.conf` file and add

```
kern.sysv.shmmax=2200158208
kern.sysv.shmall=2200158208
```

Restart postgresql and create the aatams user and the aatams database using a PostGIS enabled database as the template

`$ createuser aatams`
`$ createdb -O aatams -T postgis_template aatams`

Jon has had some success merely restoring at this point, I however did not, to avoid collisions and errors here are my steps.

First start the AATAMS application, the migrations will run and create the database structure including functions and views in the correct order.

`$ grails run-app`

Then create a TOC from the database dump file

`$ pg_restore -l db.aatams3.20130808.dump > db.aatams3.20130808.dump.list`

Edit the TOC file and comment out each line by starting it with a `;` as you will end up excluding more items than you will include. You will want to set the hibernate schema, in the TOC find `SEQUENCE SET public hibernate_sequence aatams` and uncomment that line.

Next you want to restore table data belonging to the aatams user search for entries like `TABLE DATA public animal_measurement aatams` where `animal_measurement` is the table name in this example, uncomment those entries, these are what will be restored.

When you are happy with the TOC to be restored execute a command like `pg_restore -O -j 4 -a -L db.aatams3.20130808.dump.list --disable-triggers -S postgres -d aatams db.aatams3.20130808.dump`

Where;
`-j` is the number of jobs there are few factors to consider here, please consult the `man` page, I used 4 because I have 4 cores.

`-a` means restore data only

`-L` means use this TOC to determine what should be restored, read the `man` page to see how this works with `-a`, I am being paranoid

`--disable-triggers` is fairly self explanatory, it turns off constraints during import so the order in which data is restored does not cause referential integrity checks

`-S` is the superuser account to invoke/use when disabling triggers

`-d` is the database to restore the data too

and lastly the dump file to restore the data from.

This approach worked for me with no errors being thrown at import.
