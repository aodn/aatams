# Backup
Backups are performed automatically, every second day.  There are two components to the backup:

1. the postgresql database
2. the uploaded files stored on the filesystem

The backups can be retrieved from: `backups.aodn.org.au:/var/backup/backup`.

# Restore
## Restoring the PostgreSql database

See [this restore script](restore_db.sh) for an example of how to restore the AATAMS database from backup.

It assumes that you have an already provisioned database (i.e. by chef, and a node such as `4-nec-mel.emii.org.au`).

## Restoring the uploaded files
TODO