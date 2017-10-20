Create table with dummy value
===

    create_namespace 'bugs'
    
    disable 'bugs:flink'
    drop 'bugs:flink'
    create 'bugs:flink', { NAME => 'v', VERSIONS => 1, COMPRESSION => 'GZ' }
    put 'bugs:flink', 'row', 'v:column', '>>>>>>>>>>>>>> The value we stored in HBase <<<<<<<<<<<<<<<<<'

    scan 'bugs:flink'
