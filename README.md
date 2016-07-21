Library used
   GSON: For parsing JSON

This data is from Pradeep branch

minSdk and target sdk version is 4.0

Recycler View is used to display the list
List has got 4 views
Scenarios
  1. All data is present (title, description and Image)
  2. Title and description is present
  3. Title and Image is present
  4. No data is present
  
  <b>Scenario 1 and 2 image</b>
  ![Scenario_1&2] (http://s32.postimg.org/70yuy77mt/1_2.png)

  <b>Scenario 3</b>
  ![Scenario_3] (http://s32.postimg.org/6yjmcn5ut/image.png)
  
  <b>Scenario 4</b>
  ![Scenario_4] (http://s32.postimg.org/uh04csvad/image.png)
  
  <b><i>Fetching the data from server and caching is done using File caching. Data fetched from server can be done using voley and retrofit also
  over here created own connection to handle the same only thing missing is Thread pool
  which can be achieved by ThreadPoolExecutor instance that manages a pool of thread with a task queue
  and communication is done using Handler however ExecutorService is used in this project for loading the images lazily</i></b>
  
