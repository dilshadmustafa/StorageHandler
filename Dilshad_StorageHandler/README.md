StorageHandler
===================

Introduction
-------------

StorageHandler (IStorageHandler.java) is the interface to a Storage system to access the data stored in the storage system, copy Object/file data between local filesystem and Storage system, check if Object/file exists, delete Object/file if exists, etc. 

Storage system can be any distributed storage system, Object storage system (HTTP, REST, S3-interface), distributed filesystem, NFS, FUSE, etc. StorageHandler can be implemented for Storage system of your choice: Scality, RedHat CephFS, OpenIO, OrangeFS, RedHat Gluster, SeaweedFS, Minio, IBM Cleversafe, etc.

Copyright
-------------------

Copyright (c) Dilshad Mustafa 2016.

License
-------------

Please refer LICENSE.txt file for complete details on the license and terms and conditions.

About The Author
--------------------

Dilshad Mustafa is the creator and programmer of Scabi Cluster Computing framework for BigData processing, Ensemble Machine Learning and Map/Reduce in pure Java. He is also Author of Book titled “Tech Job 9 to 9”. He is a Senior Software Architect with 16+ years experience in Information Technology industry. He has experience across various domains, Banking, Retail, Materials & Supply Chain.

He completed his B.E. in Computer Science & Engineering from Annamalai University, India and completed his M.Sc. in Communication & Network Systems from Nanyang Technological University, Singapore.
