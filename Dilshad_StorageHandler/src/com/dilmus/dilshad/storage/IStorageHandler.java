/**
 * @author Dilshad Mustafa
 * Copyright (c) Dilshad Mustafa
 * Created 19-Oct-2016
 * File Name : IStorageHandler.java
 * 
 * This interface IStorageHandler (IStorageHandler.java) is created, designed and programmed by Dilshad Mustafa
 * 
 * This file is licensed under Apache License Version 2.0.
 */
package com.dilmus.dilshad.storage;

/**
 * @author Dilshad Mustafa
 *
 */
public interface IStorageHandler {

	/* 
	 * Copyright (c) Dilshad Mustafa 2016
	 */
	
	/*
	 * Method : copyFromLocal(String storageFilePath, String localFilePath)
	 * 
	 * Assumptions for Storage system that support creation of directory given directory name
	 * Parameter Name : storageFilePath
	 * Parameter Value :
	 * 		Any standard file system path for file, example /home/<user>/testdata/storage/mydata_1/meta_data/page-0.dat
	 * 
	 * If the file already exist in the Storage System, don't throw exception. Refer below if file already exists.
	 * -----------------------------------------------------------------------------------------------------------
	 * Assumptions for Storage system that does not support creation of directory given directory name
	 * Parameter Name : storageFilePath
	 * Parameter Value :
	 * 		(1) <AnyDummyStringWithoutSlash>/<ArrayFolder>/<PageFolder>/<PageFileName>
	 * 		OR
	 * 		(2) <AnyDummyStringWithoutSlash>/<SomeFileName>
	 * 		
	 * 		where <AnyDummyStringWithoutSlash> = <AppId> or "" (empty string) or any dummy string without File.separator ("/" or "\")
	 * 
	 * Construct file name of file to be created from storageFilePath as follows (refer Parameter Value of storageFilePath above)
	 * 
	 * (1) File name to create = <ArrayFolder>_<PageFolder>_<PageFileName> [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * OR
	 * (2) File name to create = <SomeFileName> [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * 
	 * Note : If any other character is used in place of '_' used in file name above, then it should be 
	 * consistently used in other methods as well.
	 * 
	 * Example (1) : if storageFilePath = "appid1/mydata_1_app1/meta_data/page-0.dat"
	 * 			     then <AnyDummyStringWithoutSlash> = "appid1"
	 * 			     and <ArrayFolder> = "mydata_1_app1" <PageFolder> = "meta_data" <PageFileName> = "page-0.dat"
	 * So File name to create = "mydata_1_app1_meta_data_page-0.dat" [appid1 can also be appended if needed]
	 * 
	 * Example (2) : if storageFilePath = "appid1/myfile.txt"
	 * 				 then <AnyDummyStringWithoutSlash> = "appid1"
	 * 				 and <SomeFileName> = "myfile.txt"
	 * So File name to create = "myfile.txt" [appid1 can also be appended if needed]
	 * 
	 * If the file already exist in the Storage System, don't throw exception. Refer below if file already exists.
	 * -----------------------------------------------------------------------------------------------------------
	 * If the file already exists in the Storage System, handle as detailed below:-
	 * 
	 * (A) For Storage systems with S3 interface, SeaweedFS, etc.:-
	 * 
	 * Create a new Object/File entry. Treat this as a new Object/File upload and create a new Object Id/File Id 
	 * entry and store filepath/filename to Object Id/File Id mapping in Cassandra. 
	 * 
	 * Additional fields that can be included in the mapping can be IP Address and Port Number if using multiple 
	 * storage systems of same or different type.
	 * 
	 * You may delete Object/File data associated with previous Object Id/File Id mapping to this filepath/filename 
	 * and ignore any exception/error from the delete operation.
	 * 
	 * (B) For file meta-data based Storage systems, Apache HDFS, etc.:-
	 * 
	 * Recreate the file entry by deleting the existing file entry and create a new file.
	 * -----------------------------------------------------------------------------------------------------------
	 * If Cassandra is used to store filepath/filename to Object Id/File Id mapping:-
	 * 
	 * Let N be the Replication Factor, the number of replica nodes containing replica of each row of data.
	 * Let R be the number of replica nodes required to do a Read operation to successfully complete a Read query. 
	 * (Set by Consistency level ONE, QUORUM, ANY, ALL)
	 * Let W be the number of replica nodes required to do a Write a operation to successfully complete a Write 
	 * query. (Set by Consistency level ONE, QUORUM, ANY, ALL)
	 * 
	 * Then R + W > N results in a strongly consistent system.
	 * 
	 * Let Replication Factor N = 100. At Consistency level of QUORUM, R = N/2 + 1 = 51, W = N/2 + 1 = 51.
	 * R + W = 102 > 100 will result in strong consistency.
	 * 
	 * For a Write operation to succeed, minimum 51 replica nodes are required to do a Write operation to 
	 * successfully complete a Write query. Which implies (100-51) = 49 replica nodes need not answer the Write 
	 * operation. So the Cassandra cluster can tolerate up to (100 - 51) = 49 replica nodes failures for 
	 * each row of data for Write operation.
	 * 
	 * Similarly, for a Read operation to succeed, minimum 51 replica nodes are required to do a Read operation 
	 * to successfully complete a Read query. Which implies (100-51) = 49 replica nodes need not answer the Read 
	 * operation. So the Cassandra cluster can tolerate up to (100 - 51) = 49 replica nodes failures for 
	 * each row of data for Read operation.
	 * 
	 * So for Read or Write operation, the Cassandra cluster can tolerate up to (100 - 51) = 49 replica nodes 
	 * failures for each row of data.
	 * 
	 * Since only file meta-data is stored (filepath/filename to Object Id/File Id mapping), a high number 
	 * for Replication Factor can be configured.
	 * 
	 */
	public int copyFromLocal(String storageFilePath, String localFilePath) throws Exception;
	
	/* 
	 * Method : copyIfExistsToLocal(String storageFilePath, String localFilePath)
	 * 
	 * Assumptions for Storage system that support creation of directory given directory name
	 * Parameter : storageFilePath
	 * Parameter Value :
	 * 		Any standard file system path for file, example /home/<user>/testdata/mount_directory_storage/mydata_1/meta_data/page-0.dat
	 * 
	 * If the file doesn't exist in the Storage System, don't throw exception
	 * -----------------------------------------------------------------------------------------------------------
	 * Assumptions for Storage system that does not support creation of directory given directory name
	 * Parameter : storageFilePath
	 * Parameter Value:
	 * 		(1) <AnyDummyStringWithoutSlash>/<ArrayFolder>/<PageFolder>/<PageFileName>
	 * 		OR
	 * 		(2) <AnyDummyStringWithoutSlash>/<SomeFileName>
	 * 		
	 * 		where <AnyDummyStringWithoutSlash> = <AppId> or "" (empty string) or any dummy string without File.separator ("/" or "\")
	 * 
	 * Construct file name of file to be read from storageFilePath as follows (refer Parameter Value of storageFilePath above)
	 * 
	 * (1) File name to read = <ArrayFolder>_<PageFolder>_<PageFileName> [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * OR
	 * (2) File name to read = <SomeFileName> [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * 
	 * Example (1) : if storageFilePath = "appid1/mydata_1_app1/meta_data/page-0.dat"
	 * 				 then <AnyDummyStringWithoutSlash> = "appid1"
	 * 				 and <ArrayFolder> = "mydata_1_app1" <PageFolder> = "meta_data" <PageFileName> = "page-0.dat"
	 * So File name to read = "mydata_1_app1_meta_data_page-0.dat" [appid1 can also be appended if needed]
	 * 
	 * Example (2) : if storageFilePath = "appid1/myfile.txt"
	 * 				 then <AnyDummyStringWithoutSlash> = "appid1"
	 * 				 and <SomeFileName> = "myfile.txt"
	 * So File name to read = "myfile.txt" [appid1 can also be appended if needed]
	 *  
	 * If the file doesn't exist in the Storage System, don't throw exception
	 */
	public int copyIfExistsToLocal(String storageFilePath, String localFilePath) throws Exception;

	/* 
	 * Method : deleteIfExists(String storageFilePath)
	 * 
	 * Assumptions for Storage system that support creation of directory given directory name
	 * Parameter Name : storageFilePath
	 * Parameter Value :
	 * 		Any standard file system path for file, example /home/<user>/testdata/mount_directory_storage/mydata_1/meta_data/page-0.dat
	 * 
	 * If the file doesn't exist in the Storage System, don't throw exception
	 * -----------------------------------------------------------------------------------------------------------
	 * Assumptions for Storage system that does not support creation of directory given directory name
	 * Parameter Name : storageFilePath
	 * Parameter Value :
	 * 		(1) <AnyDummyStringWithoutSlash>/<ArrayFolder>/<PageFolder>/<PageFileName>
	 * 		OR
	 * 		(2) <AnyDummyStringWithoutSlash>/<SomeFileName>
	 * 
	 * 		where <AnyDummyStringWithoutSlash> = <AppId> or "" (empty string) or any dummy string without File.separator ("/" or "\")
	 * 
	 * Construct file name of file to be deleted from storageFilePath as follows (refer Parameter Value of storageFilePath above)
	 * 
	 * (1) File name to delete = <ArrayFolder>_<PageFolder>_<PageFileName> [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * OR
	 * (2) File name to delete = <SomeFileName> [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * 
	 * Example (1) : if storageFilePath = "appid1/mydata_1_app1/meta_data/page-0.dat"
	 * 				 then <AnyDummyStringWithoutSlash> = "appid1"
	 * 				 and <ArrayFolder> = "mydata_1_app1" <PageFolder> = "meta_data" <PageFileName> = "page-0.dat"
	 * So File name to delete = "mydata_1_app1_meta_data_page-0.dat" [appid1 can also be appended if needed]
	 * 
	 * Example (2) : if storageFilePath = "appid1/myfile.txt"
	 * 				 then <AnyDummyStringWithoutSlash> = "appid1"
	 * 				 and <SomeFileName> = "myfile.txt"
	 * So File name to delete = "myfile.txt" [appid1 can also be appended if needed]
	 * 
	 * If the file doesn't exist in the Storage System, don't throw exception
	 */	
	public int deleteIfExists(String storageFilePath) throws Exception;

	/* 
	 * Method : mkdirIfAbsent(String dirPath)
	 * 
	 * Assumptions for Storage system that support creation of directory given directory name
	 * Parameter Name : dirPath
	 * Parameter Value :
	 * 		Any standard file system path for directory, example /home/<user>/testdata/storage
	 * 
	 * Create directory as per dirPath, return 0 if successful else throw exception
	 * -----------------------------------------------------------------------------------------------------------
	 * Assumptions for Storage system that does not support creation of directory given directory name
	 * Parameter Name : dirPath
	 * Parameter Value :
	 * 		<AnyDummyStringWithoutSlash>/<ArrayFolder>
	 * 		
	 * 		where <AnyDummyStringWithoutSlash> = <AppId> or "" (empty string) or any dummy string without File.separator ("/" or "\")
	 * 
	 * do nothing and return 0
	 */
	public int mkdirIfAbsent(String dirPath) throws Exception;
	
	/* 
	 * Method : deleteArrayDirIfExists(String dirPath)
	 * 
	 * Assumptions for Storage system that support creation of directory given directory name
	 * Parameter Name : dirPath
	 * Parameter Value :
	 * 		Any standard file system path for directory, example /home/<user>/testdata/storage
	 * 
	 * Delete entire directory contents of dirPath, all files and sub directories contained within dirPath
	 * Do not complain saying "Directory is not empty" 
	 * If the directory dirPath doesn't exist in the Storage System, don't throw exception
	 * -----------------------------------------------------------------------------------------------------------
	 * Assumptions for Storage system that does not support creation of directory given directory name
	 * Parameter Name : dirPath
	 * Parameter Value :
	 * 		<AnyDummyStringWithoutSlash>/<ArrayFolder>
	 * 		
	 * 		where <AnyDummyStringWithoutSlash> = <AppId> or "" (empty string) or any dummy string without File.separator ("/" or "\")
	 * 
	 * Construct page file names for meta_data, index, data as follows:
	 * 		meta_data page file names = <ArrayFolder>_meta_data_page-<n>.dat (n = 0, 1, 2, 3, ...) [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * 		index page file names = <ArrayFolder>_index_page-<n>.dat (n = 0, 1, 2, 3, ...) [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * 		data page file names = <ArrayFolder>_data_page-<n>.dat (n = 0, 1, 2, 3, ...) [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * 
	 * Delete all these files with file names <ArrayFolder>_meta_data_page-<n>.dat if the file exists (n = 0, 1, 2, 3, ...) [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * Delete all these files with file names <ArrayFolder>_index_page-<n>.dat if the file exists (n = 0, 1, 2, 3, ...) [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * Delete all these files with file names <ArrayFolder>_data_page-<n>.dat if the file exists (n = 0, 1, 2, 3, ...) [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * 
	 * Example : if dirPath = "appid1/mydata_1_app1"
	 * 			 then <AnyDummyStringWithoutSlash> = "appid1"
	 * 			 and <ArrayFolder> = "mydata_1_app1"
	 * 
	 * Construct meta_data page file names as follows:
	 * 		meta_data page file names = "mydata_1_app1_meta_data_page-0.dat" (similarly do for n = 1, 2, 3, ...) [appid1 can also be appended if needed]
	 * 
	 * Construct index page file names as follows:
	 * 		index page file names = "mydata_1_app1_index_page-0.dat" (similarly do for n = 1, 2, 3, ...) [appid1 can also be appended if needed]
	 * 
	 * Construct data page file names as follows:
	 * 		data page file names = "mydata_1_app1_data_page-0.dat" (similarly do for n = 1, 2, 3, ...) [appid1 can also be appended if needed]
	 * 
	 * Delete files with meta_data page file names, index page file names, data page file names
	 * 
	 * If the files don't exist in the Storage System, don't throw exception
	 */
	public int deleteArrayDirIfExists(String dirPath) throws Exception;
	
	/* 
	 * Method : deleteDirIfExists(String dirPath)
	 * 
	 * Assumptions for Storage system that support creation of directory given directory name
	 * Parameter Name : dirPath
	 * Parameter Value :
	 * 		Any standard file system path for directory, example /home/<user>/testdata/storage
	 * 
	 * Delete entire directory contents of dirPath, all files and sub directories contained within dirPath
	 * Do not complain saying "Directory is not empty" 
	 * If the directory dirPath doesn't exist in the Storage System, don't throw exception
	 * -----------------------------------------------------------------------------------------------------------
	 * Assumptions for Storage system that does not support creation of directory given directory name
	 * Parameter Name : dirPath
	 * Parameter Value :
	 * 		<AnyDummyStringWithoutSlash>/<SomeDirectoryName>
	 * 		
	 * 		where <AnyDummyStringWithoutSlash> = <AppId> or "" (empty string) or any dummy string without File.separator ("/" or "\")
	 * 
	 * Delete all the files associated with <SomeDirectoryName> for example file names starting with <SomeDirectoryName> [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * 
	 * If the files don't exist in the Storage System, don't throw exception
	 * 
	 * If this functionality is not possible in the storage system (for example listing all the files with file names starting with <SomeDirectoryName>)
	 * then throw new Exception("Not Supported Exception");
	 */
	public int deleteDirIfExists(String dirPath) throws Exception;
	
	/* 
	 * Method : isFileExists(String storageFilePath)
	 * 
	 * Assumptions for Storage system that support creation of directory given directory name
	 * Parameter Name : storageFilePath
	 * Parameter Value :
	 * 		Any standard file system path for file, example /home/<user>/testdata/mount_directory_storage/mydata_1/meta_data/page-0.dat
	 * 
	 * If the file doesn't exist in the Storage System, return false else return true
	 * -----------------------------------------------------------------------------------------------------------
	 * Assumptions for Storage system that does not support creation of directory given directory name
	 * Parameter Name : storageFilePath
	 * Parameter Value :
	 * 		(1) <AnyDummyStringWithoutSlash>/<ArrayFolder>/<PageFolder>/<PageFileName>
	 * 		OR
	 * 		(2) <AnyDummyStringWithoutSlash>/<SomeFileName>
	 * 		
	 * 		where <AnyDummyStringWithoutSlash> = <AppId> or "" (empty string) or any dummy string without File.separator ("/" or "\")
	 * 
	 * Construct file name of file to be checked from storageFilePath as follows (refer Parameter Value of storageFilePath above)
	 * 
	 * (1) File name to check = <ArrayFolder>_<PageFolder>_<PageFileName> [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * OR
	 * (2) File name to check = <SomeFileName> [<AnyDummyStringWithoutSlash> can also be appended if needed]
	 * 
	 * Example (1) : if storageFilePath = "appid1/mydata_1_app1/meta_data/page-0.dat"
	 * 				 then <AnyDummyStringWithoutSlash> = "appid1"
	 * 				 and <ArrayFolder> = "mydata_1_app1" <PageFolder> = "meta_data" <PageFileName> = "page-0.dat"
	 * So File name to check = "mydata_1_app1_meta_data_page-0.dat" [appid1 can also be appended if needed]
	 * 
	 * Example (2) : if storageFilePath = "appid1/myfile.txt"
	 * 				 then <AnyDummyStringWithoutSlash> = "appid1"
	 * 				 and <SomeFileName> = "myfile.txt"
	 * So File name to check = "myfile.txt" [appid1 can also be appended if needed]
	 * 
	 * If the file doesn't exist in the Storage System, return false else return true
	 */
	public boolean isFileExists(String storageFilePath) throws Exception;
	
	public void close() throws Exception;
	
}
