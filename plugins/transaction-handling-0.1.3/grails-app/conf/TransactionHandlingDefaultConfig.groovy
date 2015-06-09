//import org.springframework.transaction.TransactionDefinition

grails {
    plugin {
        transactionHandling {
            
            /* AbstractPlatformTransactionManager default settings. 
               It affects any transaction started through a transaction manager. */            
            global {
               //timeout 
            }
            
            /* DomainClass.withTransaction/withNewTransaction default settings. */
            programmatic {
               // isolation =
               // timeout =
               // readOnly =
            }
            
            /* @Transactional default settings. */
            declarative {
               // isolation =
               // timeout =
               // rollbackFor =
               // noRollbackFor =
            }
            
            /* Service default settings. */
            implicit {
               // isolation =
               // timeout =
               // readOnly =
               // rollbackFor =
               // noRollbackFor = 
            }
        }
    }
}