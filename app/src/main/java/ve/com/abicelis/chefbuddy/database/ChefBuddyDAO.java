package ve.com.abicelis.chefbuddy.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ve.com.abicelis.chefbuddy.database.exceptions.CouldNotGetDataException;
import ve.com.abicelis.chefbuddy.model.Measurement;
import ve.com.abicelis.chefbuddy.model.PreparationTimeType;
import ve.com.abicelis.chefbuddy.model.Ingredient;
import ve.com.abicelis.chefbuddy.model.Recipe;


/**
 * Created by abicelis on 3/7/2017.
 */
public class ChefBuddyDAO {

    private ChefBuddyDbHelper mDatabaseHelper;

    public ChefBuddyDAO(ChefBuddyDbHelper chefBuddyDbHelper) {
        mDatabaseHelper = chefBuddyDbHelper;
    }


    /**
     * Returns the List of Recipes stored in the database without the
     */
    public List<Recipe> getRecipes() throws CouldNotGetDataException {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor cursor = db.query(ChefBuddyContract.RecipeTable.TABLE_NAME, null, null, null, null, null, null);

        try {
            while(cursor.moveToNext()) {
                Recipe recipe = getRecipeFromCursor(cursor);
                recipe.setIngredients(getIngredientsOfRecipe(recipe.getId()));
                // TODO: 7/7/2017 Add images!

                recipes.add(recipe);
            }
        } finally {
            cursor.close();
        }
        return recipes;
    }


    /**
     * Returns the list of ingredients of a recipe
     * @param recipeId The ID of the recipe
     * @return A List of ingredients
     */
    public List<Ingredient> getIngredientsOfRecipe(int recipeId) throws CouldNotGetDataException {
        List<Ingredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        String tables = String.format("%1$s INNER JOIN %2$s ON %1$s.%3$s = %2$s.%4$s",
                ChefBuddyContract.RecipeIngredientTable.TABLE_NAME,
                ChefBuddyContract.IngredientTable.TABLE_NAME,
                ChefBuddyContract.RecipeIngredientTable._ID,
                ChefBuddyContract.IngredientTable._ID);

        Cursor cursor = db.query(tables, null, ChefBuddyContract.RecipeIngredientTable.COL_NAME_RECIPE_FK.getName()+"=?", new String[]{String.valueOf(recipeId)}, null, null, null);

        try {
            while (cursor.moveToNext()) {
                Ingredient ingredient = getIngredientFromCursor(cursor);
                ingredients.add(ingredient);
            }
        } finally {
            cursor.close();
        }

        return ingredients;
    }


//
//
//    /* Get data from database */
//    /**
//     * Returns a List of Tasks (with Reminder and Attachments) which have TaskStatus.UNPROGRAMMED. Sorted Alphabetically by Task Title
//     * @return A List of TaskViewModel
//     */
//    public List<TaskViewModel> getUnprogrammedTasks() throws CouldNotGetDataException {
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        List<TaskViewModel> result = new ArrayList<>();
//        List<Task> tasks = new ArrayList<>();
//
//        Cursor cursor = db.query(ChefBuddyContract.TaskTable.TABLE_NAME,
//                null, ChefBuddyContract.TaskTable.COLUMN_NAME_STATUS.getName() + "=?",
//                new String[]{TaskStatus.UNPROGRAMMED.name()}, null, null, null);
//
//        try {
//            while (cursor.moveToNext()) {
//                Task current = getTaskFromCursor(cursor);
//
//                //Try to get the attachments, if there are any
//                current.setAttachments(getAttachmentsOfTask(current.getId()));
//
//                //If Task !ReminderType.NONE, throw an error.
//                if(current.getReminderType() != ReminderType.NONE)
//                    throw new CouldNotGetDataException("Error, found task with TaskStatus=UNPROGRAMMED with ReminderType != NONE");
//
//                tasks.add(current);
//            }
//        } finally {
//            cursor.close();
//        }
//
//        //Sort tasks by title
//        Collections.sort(tasks, new UnprogrammedTasksByTitleComparator());
//
//        //Create viewModel
//        for (int i = 0; i < tasks.size(); i++) {
//            result.add(new TaskViewModel(tasks.get(i), TaskViewModelType.UNPROGRAMMED_REMINDER));
//        }
//
//        return result;
//    }
//
//    /**
//     * Returns a List of Tasks (with Reminder and Attachments) which have TaskStatus.PROGRAMMED AND ReminderType.LOCATION_BASED, sorted by Location
//     * @return A List of TaskViewModel
//     */
//    public List<TaskViewModel> getLocationBasedTasks(Resources resources) throws CouldNotGetDataException, InvalidClassException {
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        List<TaskViewModel> result;
//        List<Task> tasks = new ArrayList<>();
//
//        Cursor cursor = db.query(ChefBuddyContract.TaskTable.TABLE_NAME,
//                null, ChefBuddyContract.TaskTable.COLUMN_NAME_STATUS.getName() + "=?",
//                new String[]{TaskStatus.PROGRAMMED.name()}, null, null, null);
//
//        try {
//            while (cursor.moveToNext()) {
//                Task current = getTaskFromCursor(cursor);
//
//                if(current.getReminderType() != ReminderType.LOCATION_BASED)      //Skip NON location-based task
//                    continue;
//
//                //Try to get the attachments, if there are any
//                current.setAttachments(getAttachmentsOfTask(current.getId()));
//
//                //If Task ReminderType.NONE, throw an error.
//                if(current.getReminderType() == ReminderType.NONE)
//                    throw new CouldNotGetDataException("Error, Task with TaskStatus=PROGRAMMED has ReminderType=NONE");
//                else
//                    current.setReminder(getReminderOfTask(current.getId(), current.getReminderType()));
//
//                tasks.add(current);
//            }
//
//        } finally {
//            cursor.close();
//        }
//
//        //Generate List<TaskViewModel>
//        result = new TaskSortingUtil().generateProgrammedTaskHeaderList(tasks, TaskSortType.PLACE, resources);
//
//        return result;
//    }
//
//
//    /**
//     * Returns a List of Tasks (with Reminder and Attachments) which have TaskStatus.PROGRAMMED
//     * @param sortType TaskSortType enum value with which to sort results. By date or location
//     * @return A List of TaskViewModel
//     */
//    public List<TaskViewModel> getProgrammedTasks(@NonNull TaskSortType sortType, boolean includeLocationBasedTasks, Resources resources) throws CouldNotGetDataException, InvalidClassException {
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        List<TaskViewModel> result;
//        List<Task> tasks = new ArrayList<>();
//
//        Cursor cursor = db.query(ChefBuddyContract.TaskTable.TABLE_NAME,
//                null, ChefBuddyContract.TaskTable.COLUMN_NAME_STATUS.getName() + "=?",
//                new String[]{TaskStatus.PROGRAMMED.name()}, null, null, null);
//
//        try {
//            while (cursor.moveToNext()) {
//                Task current = getTaskFromCursor(cursor);
//
//                if(!includeLocationBasedTasks && current.getReminderType() == ReminderType.LOCATION_BASED)      //Skip location-based task
//                    continue;
//
//                //Try to get the attachments, if there are any
//                current.setAttachments(getAttachmentsOfTask(current.getId()));
//
//                //If Task ReminderType.NONE, throw an error.
//                if(current.getReminderType() == ReminderType.NONE)
//                    throw new CouldNotGetDataException("Error, Task with TaskStatus=PROGRAMMED has ReminderType=NONE");
//                else
//                    current.setReminder(getReminderOfTask(current.getId(), current.getReminderType()));
//
//                tasks.add(current);
//            }
//
//        } finally {
//            cursor.close();
//        }
//
//        //Generate List<TaskViewModel>
//        result = new TaskSortingUtil().generateProgrammedTaskHeaderList(tasks, sortType, resources);
//
//        return result;
//    }
//
//    /**
//     * Returns a List of Tasks (with Reminder and Attachments) which have TaskStatus.DONE
//     * @param sortType TaskSortType enum value with which to sort results. By date or location
//     * @return A List of TaskViewModel
//     */
//    public List<TaskViewModel> getDoneTasks(@NonNull TaskSortType sortType, Resources resources) throws CouldNotGetDataException, InvalidClassException {
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        List<TaskViewModel> result = new ArrayList<>();
//        List<Task> tasks = new ArrayList<>();
//
//        Cursor cursor = db.query(ChefBuddyContract.TaskTable.TABLE_NAME,
//                null, ChefBuddyContract.TaskTable.COLUMN_NAME_STATUS.getName() + "=?",
//                new String[]{TaskStatus.DONE.name()}, null, null, null);
//
//        try {
//            while (cursor.moveToNext()) {
//                Task current = getTaskFromCursor(cursor);
//
//                //Try to get the attachments, if there are any
//                current.setAttachments(getAttachmentsOfTask(current.getId()));
//
//                //If Task has reminder, get it
//                if(current.getReminderType() != ReminderType.NONE)
//                    current.setReminder(getReminderOfTask(current.getId(), current.getReminderType()));
//
//                tasks.add(current);
//            }
//
//        } finally {
//            cursor.close();
//        }
//
//        //Generate List<TaskViewModel> This List will be sorted and grouped!
//        result = new TaskSortingUtil().generateDoneTaskHeaderList(tasks, sortType, resources);
//
//        return result;
//    }
//
//    /**
//     * Returns a List of Tasks (Status:PROGRAMMED) which have Location-Based reminders of a particular Place, set to trigger either entering or exiting or both.
//     * @param placeId The ID of the place with which to look for Tasks
//     */
//    public List<Task> getLocationBasedTasksAssociatedWithPlace(int placeId, int geofenceTransition) throws CouldNotGetDataException {
//        List<Task> tasks = new ArrayList<>();
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        Cursor cursor = null;
//
//        switch (geofenceTransition) {
//            case -1: //Any
//                cursor = db.query(ChefBuddyContract.LocationBasedReminderTable.TABLE_NAME, null,
//                        ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_PLACE_FK.getName() + "=?",
//                        new String[] {String.valueOf(placeId)}, null, null, null);
//                break;
//
//            case Geofence.GEOFENCE_TRANSITION_DWELL:
//            case Geofence.GEOFENCE_TRANSITION_ENTER:
//                cursor = db.query(ChefBuddyContract.LocationBasedReminderTable.TABLE_NAME, null,
//                        ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_PLACE_FK.getName() + "=? AND " +
//                        ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TRIGGER_ENTERING.getName() + "=?",
//                        new String[] {String.valueOf(placeId), "true"}, null, null, null);
//                break;
//
//
//            case Geofence.GEOFENCE_TRANSITION_EXIT:
//                cursor = db.query(ChefBuddyContract.LocationBasedReminderTable.TABLE_NAME, null,
//                        ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_PLACE_FK.getName() + "=? AND " +
//                                ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TRIGGER_EXITING.getName() + "=?",
//                        new String[] {String.valueOf(placeId), "true"}, null, null, null);
//                break;
//        }
//
//        if(cursor != null) {
//            try {
//                while (cursor.moveToNext()) {
//                    int taskId = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TASK_FK.getName()));
//                    Task task = getTask(taskId);
//                    if(task.getStatus().equals(TaskStatus.PROGRAMMED))
//                        tasks.add(task);
//                }
//            } finally {
//                cursor.close();
//            }
//        }
//
//
//        return tasks;
//    }
//
//
//    /**
//     * Returns the next PROGRAMMED task(With ONE-TIME or REPEATING reminder) to occur
//     * @param alreadyTriggeredTaskList an optional task list to not include in the search
//     * @return A single TaskTriggerViewModel or null of there are no tasks
//     */
//    public TaskTriggerViewModel getNextTaskToTrigger(@NonNull List<Integer> alreadyTriggeredTaskList) throws CouldNotGetDataException {
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        Task nextTaskToTrigger = null;
//        Calendar triggerDate = null;
//        Time triggerTime = null;
//
//        Cursor cursor = db.query(ChefBuddyContract.TaskTable.TABLE_NAME,
//                null, ChefBuddyContract.TaskTable.COLUMN_NAME_STATUS.getName() + "=?",
//                new String[]{TaskStatus.PROGRAMMED.name()}, null, null, null);
//
//        try {
//            while (cursor.moveToNext()) {
//                Task current = getTaskFromCursor(cursor);
//
//                if(alreadyTriggeredTaskList.contains(current.getId()))   //Skip
//                    continue;
//
//                try {
//                    current.setReminder(getReminderOfTask(current.getId(), current.getReminderType()));
//                }catch (CouldNotGetDataException | SQLiteConstraintException e ) {
//                    throw new CouldNotGetDataException("Error fetching reminder for task ID" + current.getId(), e);
//                }
//
//                //TODO: this filter could be made on db query.
//                if( current.getReminderType().equals(ReminderType.ONE_TIME) ||  current.getReminderType().equals(ReminderType.REPEATING) ) {
//
//                    if(TaskUtil.checkIfOverdue(current.getReminder()))  //Skip overdue reminders
//                        continue;
//
//                    if(nextTaskToTrigger == null) {
//                        nextTaskToTrigger = current;
//                        triggerDate = (current.getReminderType().equals(ReminderType.ONE_TIME) ? ((OneTimeReminder)current.getReminder() ).getDate() : TaskUtil.getRepeatingReminderNextCalendar( (RepeatingReminder)current.getReminder()) );
//                        triggerTime = (current.getReminderType().equals(ReminderType.ONE_TIME) ? ((OneTimeReminder)current.getReminder() ).getTime() : ((RepeatingReminder)current.getReminder()).getTime() );
//                        continue;
//                    }
//
//                    if(current.getReminderType().equals(ReminderType.ONE_TIME)) {
//                        OneTimeReminder otr = (OneTimeReminder)current.getReminder();
//                        Calendar currentDate = CalendarUtil.getCalendarFromDateAndTime(otr.getDate(), otr.getTime());
//
//                        if(currentDate.compareTo(triggerDate) < 0 ) {
//                            nextTaskToTrigger = current;
//                            triggerDate = currentDate;
//                            triggerTime = otr.getTime();
//                            continue;
//                        }
//                    }
//
//                    if(current.getReminderType().equals(ReminderType.REPEATING)) {
//                        RepeatingReminder rr = (RepeatingReminder)current.getReminder();
//                        Calendar currentDate = TaskUtil.getRepeatingReminderNextCalendar(rr);
//
//                        if(currentDate == null) continue;   //Overdue
//
//                        if(currentDate.compareTo(triggerDate) < 0 ) {
//                            nextTaskToTrigger = current;
//                            triggerDate = currentDate;
//                            triggerTime = rr.getTime();
//                            continue;
//                        }
//                    }
//
//                }
//            }
//        } finally {
//            cursor.close();
//        }
//
//        if(nextTaskToTrigger == null) return null;
//        return new TaskTriggerViewModel(nextTaskToTrigger, triggerDate, triggerTime);
//    }
//
//
//    /**
//     * Returns a Task (with Reminder and Attachments) given a taskId
//     * @param taskId    The ID of the Task to get.
//     */
//    public Task getTask(int taskId) throws CouldNotGetDataException, SQLiteConstraintException {
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        Cursor cursor = db.query(ChefBuddyContract.TaskTable.TABLE_NAME, null, ChefBuddyContract.PlaceTable._ID + "=?",
//                new String[]{String.valueOf(taskId)}, null, null, null);
//
//        if (cursor.getCount() == 0)
//            throw new CouldNotGetDataException("Specified Task not found in the database. Passed id=" + taskId);
//        if (cursor.getCount() > 1)
//            throw new SQLiteConstraintException("Database UNIQUE constraint failure, more than one record found. Passed value=" + taskId);
//
//        cursor.moveToNext();
//        Task task = getTaskFromCursor(cursor);
//        task.setAttachments(getAttachmentsOfTask(taskId));
//
//        if(task.getReminderType() != ReminderType.NONE)
//            task.setReminder(getReminderOfTask(taskId, task.getReminderType()));
//        return task;
//    }
//
//
//    /**
//     * Returns a List of all the Places in the database.
//     */
//    public List<Place> getPlaces() {
//        List<Place> places = new ArrayList<>();
//
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        Cursor cursor = db.query(ChefBuddyContract.PlaceTable.TABLE_NAME, null, null, null, null, null, null);
//
//        try {
//            while (cursor.moveToNext()) {
//                places.add(getPlaceFromCursor(cursor));
//            }
//        } finally {
//            cursor.close();
//        }
//
//        return places;
//    }
//
//    /**
//     * Returns a List of Places associated with PROGRAMMED tasks with location-based reminders
//     */
//    public List<Place> getActivePlaces() {
//        List<Place> places = new ArrayList<>();
//        int taskCount;
//        Place place;
//
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        Cursor cursor = db.query(ChefBuddyContract.PlaceTable.TABLE_NAME, null, null, null, null, null, null);
//
//        try {
//            while (cursor.moveToNext()) {
//                place = getPlaceFromCursor(cursor);
//
//                try {
//                    taskCount = getLocationBasedTasksAssociatedWithPlace(place.getId(), -1).size();
//                } catch (CouldNotGetDataException e) {
//                    taskCount = 0;
//                }
//
//                if(taskCount > 0)
//                    places.add(place);
//            }
//        } finally {
//            cursor.close();
//        }
//
//        return places;
//    }
//
//
//
//
//
//    /**
//     * Returns a Place given a placeId.
//     * @param placeId The id of the place
//     */
//    public Place getPlace(int placeId) throws PlaceNotFoundException, SQLiteConstraintException {
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        Cursor cursor = db.query(ChefBuddyContract.PlaceTable.TABLE_NAME, null, ChefBuddyContract.PlaceTable._ID + "=?",
//                new String[]{String.valueOf(placeId)}, null, null, null);
//
//        if (cursor.getCount() == 0)
//            throw new PlaceNotFoundException("Specified Place not found in the database. Passed id=" + placeId);
//        if (cursor.getCount() > 1)
//            throw new SQLiteConstraintException("Database UNIQUE constraint failure, more than one record found. Passed value=" + placeId);
//
//        cursor.moveToNext();
//        return getPlaceFromCursor(cursor);
//    }
//
//
//    /**
//     * Returns a List of Attachments associated to a Task.
//     * @param taskId The id of the Task
//     */
//    public ArrayList<Attachment> getAttachmentsOfTask(int taskId) {
//        ArrayList<Attachment> attachments = new ArrayList<>();
//        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
//        Cursor cursor = db.query(ChefBuddyContract.AttachmentTable.TABLE_NAME, null,
//                ChefBuddyContract.AttachmentTable.COLUMN_NAME_TASK_FK.getName() + "=?",
//                        new String[]{String.valueOf(taskId)}, null, null, null);
//
//        try {
//            while (cursor.moveToNext()) {
//                attachments.add(getAttachmentFromCursor(cursor));
//            }
//        } finally {
//            cursor.close();
//        }
//
//        return attachments;
//    }
//
//
//    /**
//     * Returns a Reminder given its taskId and reminderType
//     * @param taskId The ID of the task
//     * @param reminderType The Type of reminder
//     */
//    public Reminder getReminderOfTask(int taskId, @NonNull ReminderType reminderType) throws  CouldNotGetDataException, SQLiteConstraintException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//        Reminder reminder;
//
//        String tableName, whereClause;
//        switch (reminderType) {
//            case ONE_TIME:
//                whereClause = ChefBuddyContract.OneTimeReminderTable.COLUMN_NAME_TASK_FK.getName() + " =?";
//                tableName = ChefBuddyContract.OneTimeReminderTable.TABLE_NAME;
//                break;
//            case REPEATING:
//                whereClause = ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_TASK_FK.getName() + " =?";
//                tableName = ChefBuddyContract.RepeatingReminderTable.TABLE_NAME;
//                break;
//            case LOCATION_BASED:
//                whereClause = ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TASK_FK.getName() + " =?";
//                tableName = ChefBuddyContract.LocationBasedReminderTable.TABLE_NAME;
//                break;
//            default:
//                throw new CouldNotGetDataException("ReminderType is invalid. Type=" + reminderType);
//        }
//
//        Cursor cursor = db.query(tableName, null, whereClause, new String[]{String.valueOf(taskId)}, null, null, null);
//        try {
//            if (cursor.getCount() == 0)
//                throw new CouldNotGetDataException("Specified Reminder not found in the database. Passed id=" + taskId);
//            if (cursor.getCount() > 1)
//                throw new SQLiteConstraintException("Database UNIQUE constraint failure, more than one Reminder found. Passed id=" + taskId);
//
//            cursor.moveToNext();
//            switch (reminderType) {
//                case ONE_TIME:
//                    reminder = getOneTimeReminderFromCursor(cursor);
//                    break;
//                case REPEATING:
//                    reminder = getRepeatingReminderFromCursor(cursor);
//                    break;
//                case LOCATION_BASED:
//                    reminder = getLocationBasedReminderFromCursor(cursor);
//                    int placeId = ((LocationBasedReminder)reminder).getPlaceId();
//                    try {
//                        ((LocationBasedReminder)reminder).setPlace(getPlace(placeId));
//                    } catch (PlaceNotFoundException | SQLiteConstraintException e) {
//                        throw new CouldNotGetDataException("Error trying to get Place for Location-based Reminder", e);
//                    }
//
//                    break;
//                default:
//                    throw new CouldNotGetDataException("ReminderType is invalid. Type=" + reminderType);
//            }
//        } finally {
//            cursor.close();
//        }
//
//        return reminder;
//    }
//
//
//
//
//
//
//
//
//
//
//
//    /* Delete data from database */
//
//    /**
//     * Deletes a single Place, given its ID, also deletes Location-based reminders associated with place and updates Task ReminderType to NONE
//     * @param placeId The ID of the place to delete
//     */
//    public boolean deletePlace(int placeId) throws CouldNotDeleteDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        List<Task> tasks;
//        try {
//            tasks = getLocationBasedTasksAssociatedWithPlace(placeId, -1);
//        }catch (CouldNotGetDataException e) {
//            throw new CouldNotDeleteDataException("Error getting Task list associated with Place. PlaceID=" + placeId, e);
//        }
//
//        if(tasks.size() > 0) {      //Remove Location-based reminders from task, and update task ReminderType to NONE.
//            for (Task task : tasks) {
//                deleteReminderOfTask(task.getId());
//                task.setStatus(TaskStatus.UNPROGRAMMED);
//                task.setReminderType(ReminderType.NONE);
//                try {
//                    updateTask(task);
//                } catch (CouldNotUpdateDataException e) {
//                    throw new CouldNotDeleteDataException("Error updating RemidnerType of Task to NONE. TaskID=" + task.getId(), e);
//                }
//            }
//        }
//
//        return db.delete(ChefBuddyContract.PlaceTable.TABLE_NAME,
//                ChefBuddyContract.PlaceTable._ID + " =?",
//                new String[]{String.valueOf(placeId)}) > 0;
//    }
//
//
//    /**
//     * Deletes all Attachments linked to an Task, given the task's ID
//     * @param taskId The ID of the reminder whose attachments will be deleted
//     */
//    public boolean deleteAttachmentsOfTask(int taskId) throws CouldNotDeleteDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        return db.delete(ChefBuddyContract.AttachmentTable.TABLE_NAME,
//                ChefBuddyContract.AttachmentTable.COLUMN_NAME_TASK_FK.getName() + " =?",
//                new String[]{String.valueOf(taskId)}) > 0;
//    }
//
//    /**
//     * Deletes a single attachment, given its ID
//     * @param attachmentId The ID of the attachment to be deleted
//     */
//    public boolean deleteAttachment(int attachmentId) throws CouldNotDeleteDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        return db.delete(ChefBuddyContract.AttachmentTable.TABLE_NAME,
//                ChefBuddyContract.AttachmentTable._ID + " =?",
//                new String[]{String.valueOf(attachmentId)}) > 0;
//    }
//
//
//    /**
//     * Deletes the Reminder of type ReminderType associated to a Task
//     * @param taskId The ID of the task
//     */
//    public void deleteReminderOfTask(int taskId) throws CouldNotDeleteDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        String tableName, whereClause;
//
//        whereClause = ChefBuddyContract.OneTimeReminderTable.COLUMN_NAME_TASK_FK.getName() + " =?";
//        tableName = ChefBuddyContract.OneTimeReminderTable.TABLE_NAME;
//        db.delete(tableName, whereClause, new String[]{String.valueOf(taskId)});
//
//        whereClause = ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_TASK_FK.getName() + " =?";
//        tableName = ChefBuddyContract.RepeatingReminderTable.TABLE_NAME;
//        db.delete(tableName, whereClause, new String[]{String.valueOf(taskId)});
//
//        whereClause = ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TASK_FK.getName() + " =?";
//        tableName = ChefBuddyContract.LocationBasedReminderTable.TABLE_NAME;
//        db.delete(tableName, whereClause, new String[]{String.valueOf(taskId)});
//    }
//
//
//
//
//    /**
//     * Deletes a Task and associated Attachments and Reminder
//     * @param taskId The ID of the task
//     */
//    public boolean deleteTask(int taskId) throws CouldNotDeleteDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        Task task;
//        try {
//            task = getTask(taskId);
//        } catch (CouldNotGetDataException e) {
//            throw new CouldNotDeleteDataException("Failed to get task from database. TaskID=" + taskId, e);
//        }
//
//        //Delete task's attachments
//        deleteAttachmentsOfTask(taskId);
//
//        //If task has a reminder, delete it
//        if(task.getReminderType() != ReminderType.NONE) {
//            deleteReminderOfTask(taskId);
//        }
//
//        //Finally, delete the task
//        return db.delete(ChefBuddyContract.TaskTable.TABLE_NAME,
//                ChefBuddyContract.TaskTable._ID + " =?",
//                new String[]{String.valueOf(taskId)}) > 0;
//
//    }
//
//
//
//
//
//
//
//    /* Update data on database */
//
//    /**
//     * Updates the information stored about a Place
//     * @param place The Place to update
//     */
//    public long updatePlace(Place place) throws CouldNotUpdateDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        int count = db.update(
//                ChefBuddyContract.PlaceTable.TABLE_NAME,
//                getValuesFromPlace(place),
//                ChefBuddyContract.PlaceTable._ID + " =? ",
//                new String[] {String.valueOf(place.getId())} );
//
//        return count;
//    }
//
//    /**
//     * Deletes previous attachments of a task and reinserts them
//     * @param task The Task whose attachments to delete and reinsert
//     */
//    public long[] updateAttachmentsOfTask(Task task) throws CouldNotUpdateDataException {
//        try {
//            deleteAttachmentsOfTask(task.getId());
//        } catch (CouldNotDeleteDataException e) {
//            throw new CouldNotUpdateDataException("Could not delete attachments while updating.", e.getCause());
//        }
//
//        long[] insertedRowIds;
//        try {
//            insertedRowIds = insertAttachmentsOfTask(task.getId(), task.getAttachments());
//        } catch (CouldNotInsertDataException e) {
//            throw new CouldNotUpdateDataException("Could not insert attachments while updating.", e.getCause());
//        }
//
//        return insertedRowIds;
//    }
//
//    /**
//     * Updates the information stored about a List of Attachments
//     * @param attachments The Attachments to update
//     */
//    public long[] updateAttachments(ArrayList<Attachment> attachments) throws CouldNotUpdateDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//        long[] updatedRowIds = new long[attachments.size()];
//
//        for (int i = 0; i < attachments.size(); i++)
//            updatedRowIds[i] = updateAttachment(attachments.get(i));
//
//        return updatedRowIds;
//    }
//
//    /**
//     * Updates the information stored about an Attachment
//     * @param attachment The Attachment to update
//     */
//    public long updateAttachment(Attachment attachment) throws CouldNotUpdateDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        long updatedRowIds = db.update(ChefBuddyContract.AttachmentTable.TABLE_NAME,
//                    getValuesFromAttachment(attachment),
//                    ChefBuddyContract.AttachmentTable._ID + " =? ",
//                    new String[]{String.valueOf(attachment.getId())});
//
//        return updatedRowIds;
//    }
//
//    /**
//     * Updates the information stored about a Reminder
//     * @param reminder The Reminder to update
//     */
//    public boolean updateReminderOfTask(Reminder reminder, int taskId) throws CouldNotUpdateDataException {
//
//        //Need to delete old reminder and insert new one
//        //Old and New reminderType may be different, which are stored in different tables
//        try {
//            deleteReminderOfTask(taskId);
//        }catch (CouldNotDeleteDataException e) {
//            throw new CouldNotUpdateDataException("Error while deleting old reminder in dao.updateReminder(). Reminder=" + reminder.toString(), e);
//        }
//
//        if(reminder != null) {
//            try {
//                insertReminderOfTask(taskId, reminder);
//            }catch (CouldNotInsertDataException e) {
//                throw new CouldNotUpdateDataException("Error while inserting new reminder in dao.updateReminder(). Reminder=" + reminder.toString(), e);
//            }
//        }
//
//        return true;
//    }
//
//
//    /**
//     * Updates the information stored about a Task, its Reminder and its Attachments.
//     * @param task The Task to update
//     */
//    public long updateTask(Task task) throws CouldNotUpdateDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        updateAttachmentsOfTask(task);
//
//        updateReminderOfTask(task.getReminder(), task.getId());
//
//        return db.update(
//                ChefBuddyContract.TaskTable.TABLE_NAME,
//                getValuesFromTask(task),
//                ChefBuddyContract.TaskTable._ID + " =? ",
//                new String[] {String.valueOf(task.getId())} );
//    }
//
//
//
//
//
//
//
//
//
//    /* Insert data into database */
//
//    /**
//     * Inserts a new Place into the database.
//     * @param place The Place to be inserted
//     */
//    public long insertPlace(Place place) throws CouldNotInsertDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        ContentValues values = getValuesFromPlace(place);
//
//        long newRowId;
//        newRowId = db.insert(ChefBuddyContract.PlaceTable.TABLE_NAME, null, values);
//
//        if (newRowId == -1)
//            throw new CouldNotInsertDataException("There was a problem inserting the Place: " + place.toString());
//
//        return newRowId;
//    }
//
//
//    /**
//     * Inserts a List of Attachments associated to an Task, into the database.
//     * @param taskId The id of the Task associated to the Attachments
//     * @param attachments The List of Attachments to be inserted
//     */
//    public long[] insertAttachmentsOfTask(int taskId, List<Attachment> attachments) throws CouldNotInsertDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//        long[] newRowIds = new long[attachments.size()];
//
//        for (int i = 0; i < attachments.size(); i++) {
//
//            Attachment attachment = attachments.get(i);
//            attachment.setTaskId(taskId);
//            ContentValues values = getValuesFromAttachment(attachment);
//
//            newRowIds[i] = db.insert(ChefBuddyContract.AttachmentTable.TABLE_NAME, null, values);
//
//            if (newRowIds[i] == -1)
//                throw new CouldNotInsertDataException("There was a problem inserting the Attachment: " + attachments.toString());
//        }
//
//        return newRowIds;
//    }
//
//
//    /**
//     * Inserts a new Reminder into the database.
//     * @param taskId The id of the Task associated to the Reminder
//     * @param reminder The Reminder to insert
//     */
//    public long insertReminderOfTask(int taskId, Reminder reminder) throws CouldNotInsertDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        ContentValues values;
//        String tableName;
//
//        reminder.setTaskId(taskId);
//
//        switch (reminder.getType()) {
//            case ONE_TIME:
//                values = getValuesFromOneTimeReminder((OneTimeReminder) reminder);
//                tableName = ChefBuddyContract.OneTimeReminderTable.TABLE_NAME;
//                break;
//            case REPEATING:
//                values = getValuesFromRepeatingReminder((RepeatingReminder) reminder);
//                tableName = ChefBuddyContract.RepeatingReminderTable.TABLE_NAME;
//                break;
//            case LOCATION_BASED:
//                values = getValuesFromLocationBasedReminder((LocationBasedReminder) reminder);
//                tableName = ChefBuddyContract.LocationBasedReminderTable.TABLE_NAME;
//                break;
//            default:
//                throw new CouldNotInsertDataException("ReminderType is invalid. Type=" + reminder.getType());
//        }
//
//        long newRowId;
//        newRowId = db.insert(tableName, null, values);
//
//        if (newRowId == -1)
//            throw new CouldNotInsertDataException("There was a problem inserting the Reminder: " + reminder.toString());
//
//        return newRowId;
//    }
//
//
//    /**
//     * Inserts a new Task and its associated Reminder and Attachments into the database.
//     * @param task The Task (and associated Reminder and Attachments) to insert
//     */
//    public long insertTask(Task task) throws CouldNotInsertDataException {
//        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
//
//        ContentValues values = getValuesFromTask(task);
//
//        long newRowId;
//        newRowId = db.insert(ChefBuddyContract.TaskTable.TABLE_NAME, null, values);
//
//        if (newRowId == -1)
//            throw new CouldNotInsertDataException("There was a problem inserting the Task: " + task.toString());
//
//
//
//        //Insert Attachments
//        if (task.getAttachments() != null && task.getAttachments().size() > 0) {
//            try {
//                insertAttachmentsOfTask((int)newRowId, task.getAttachments());
//            } catch (CouldNotInsertDataException e) {
//                throw new CouldNotInsertDataException("There was a problem inserting the Attachments while inserting the Task: " + task.toString(), e);
//            }
//        }
//
//        //Insert Reminder if it exists
//        if (task.getReminder() != null) {
//            try {
//                insertReminderOfTask((int)newRowId, task.getReminder());
//            } catch (CouldNotInsertDataException e) {
//                throw new CouldNotInsertDataException("There was a problem inserting the Reminder while inserting the Task: " + task.toString(), e);
//            }
//        }
//
//        return newRowId;
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//    /* Model to ContentValues */
//
//    private ContentValues getValuesFromTask(Task task) {
//        ContentValues values = new ContentValues();
//        values.put(ChefBuddyContract.TaskTable.COLUMN_NAME_STATUS.getName(), task.getStatus().name());
//        values.put(ChefBuddyContract.TaskTable.COLUMN_NAME_TITLE.getName(), task.getTitle());
//        values.put(ChefBuddyContract.TaskTable.COLUMN_NAME_DESCRIPTION.getName(), task.getDescription());
//        values.put(ChefBuddyContract.TaskTable.COLUMN_NAME_CATEGORY.getName(), task.getCategory().name());
//        values.put(ChefBuddyContract.TaskTable.COLUMN_NAME_REMINDER_TYPE.getName(), task.getReminderType().name());
//
//        values.put(ChefBuddyContract.TaskTable.COLUMN_NAME_DONE_DATE.getName(), (task.getStatus() == TaskStatus.DONE ? task.getDoneDate().getTimeInMillis() : -1));
//        return values;
//    }
//
//
//    private ContentValues getValuesFromOneTimeReminder(OneTimeReminder oneTimeReminder) {
//        ContentValues values = new ContentValues();
//        values.put(ChefBuddyContract.OneTimeReminderTable.COLUMN_NAME_TASK_FK.getName(), oneTimeReminder.getTaskId());
//        values.put(ChefBuddyContract.OneTimeReminderTable.COLUMN_NAME_DATE.getName(), oneTimeReminder.getDate().getTimeInMillis());
//        values.put(ChefBuddyContract.OneTimeReminderTable.COLUMN_NAME_TIME.getName(), oneTimeReminder.getTime().getTimeInMinutes());
//        return values;
//    }
//
//
//    private ContentValues getValuesFromRepeatingReminder(RepeatingReminder repeatingReminder) {
//        ContentValues values = new ContentValues();
//        values.put(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_TASK_FK.getName(), repeatingReminder.getTaskId());
//        values.put(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_DATE.getName(), repeatingReminder.getDate().getTimeInMillis());
//        values.put(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_TIME.getName(), repeatingReminder.getTime().getTimeInMinutes());
//        values.put(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_REPEAT_TYPE.getName(), repeatingReminder.getRepeatType().name());
//        values.put(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_REPEAT_INTERVAL.getName(), repeatingReminder.getRepeatInterval());
//        values.put(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_REPEAT_END_TYPE.getName(), repeatingReminder.getRepeatEndType().name());
//        values.put(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_REPEAT_END_NUMBER_OF_EVENTS.getName(), (repeatingReminder.getRepeatEndType() == ReminderRepeatEndType.FOR_X_EVENTS ? repeatingReminder.getRepeatEndNumberOfEvents() : -1));
//        values.put(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_REPEAT_END_DATE.getName(), (repeatingReminder.getRepeatEndType() == ReminderRepeatEndType.UNTIL_DATE ? repeatingReminder.getRepeatEndDate().getTimeInMillis() : -1));
//        return values;
//    }
//
//
//    private ContentValues getValuesFromLocationBasedReminder(LocationBasedReminder locationBasedReminder) {
//        ContentValues values = new ContentValues();
//        values.put(ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TASK_FK.getName(), locationBasedReminder.getTaskId());
//        values.put(ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_PLACE_FK.getName(), locationBasedReminder.getPlaceId());
//        values.put(ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TRIGGER_ENTERING.getName(), String.valueOf(locationBasedReminder.getTriggerEntering()));
//        values.put(ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TRIGGER_EXITING.getName(), String.valueOf(locationBasedReminder.getTriggerExiting()));
//        return values;
//    }
//
//
//    private ContentValues getValuesFromPlace(Place place) {
//        ContentValues values = new ContentValues();
//        values.put(ChefBuddyContract.PlaceTable.COLUMN_NAME_ALIAS.getName(), place.getAlias());
//        values.put(ChefBuddyContract.PlaceTable.COLUMN_NAME_ADDRESS.getName(), place.getAddress());
//        values.put(ChefBuddyContract.PlaceTable.COLUMN_NAME_LATITUDE.getName(), place.getLatitude());
//        values.put(ChefBuddyContract.PlaceTable.COLUMN_NAME_LONGITUDE.getName(), place.getLongitude());
//        values.put(ChefBuddyContract.PlaceTable.COLUMN_NAME_RADIUS.getName(), place.getRadius());
//        values.put(ChefBuddyContract.PlaceTable.COLUMN_NAME_IS_ONE_OFF.getName(), place.isOneOff());
//        return values;
//    }
//
//
//    private ContentValues getValuesFromAttachment(Attachment attachment) {
//        ContentValues values = new ContentValues();
//        values.put(ChefBuddyContract.AttachmentTable.COLUMN_NAME_TASK_FK.getName(), attachment.getTaskId());
//        values.put(ChefBuddyContract.AttachmentTable.COLUMN_NAME_TYPE.getName(), attachment.getType().name());
//
//        switch (attachment.getType()) {
//            case AUDIO:
//                values.put(ChefBuddyContract.AttachmentTable.COLUMN_NAME_CONTENT_TEXT.getName(), ((AudioAttachment) attachment).getAudioFilename());
//                break;
//            case IMAGE:
//                values.put(ChefBuddyContract.AttachmentTable.COLUMN_NAME_CONTENT_BLOB.getName(), ((ImageAttachment) attachment).getThumbnail());
//                values.put(ChefBuddyContract.AttachmentTable.COLUMN_NAME_CONTENT_TEXT.getName(), ((ImageAttachment) attachment).getImageFilename());
//                break;
//            case TEXT:
//                values.put(ChefBuddyContract.AttachmentTable.COLUMN_NAME_CONTENT_TEXT.getName(), ((TextAttachment) attachment).getText());
//                break;
//            case LIST:
//                values.put(ChefBuddyContract.AttachmentTable.COLUMN_NAME_CONTENT_TEXT.getName(), ((ListAttachment) attachment).getItemsJson());
//                break;
//            case LINK:
//                values.put(ChefBuddyContract.AttachmentTable.COLUMN_NAME_CONTENT_TEXT.getName(), ((LinkAttachment) attachment).getLink());
//                break;
//            default:
//                throw new InvalidParameterException("AttachmentType is invalid. Value = " + attachment.getType());
//        }
//        return values;
//    }
//
//
//
//
//
//
//
//
//








    /* Cursor to Model */

    private Recipe getRecipeFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.RecipeTable._ID));
        String name = cursor.getString(cursor.getColumnIndex(ChefBuddyContract.RecipeTable.COL_NAME_NAME.getName()));
        int servings = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.RecipeTable.COL_NAME_SERVINGS.getName()));
        String preparationTime = cursor.getString(cursor.getColumnIndex(ChefBuddyContract.RecipeTable.COL_NAME_PREPARATION_TIME.getName()));

        PreparationTimeType preparationTimeType;
        try {
            preparationTimeType = PreparationTimeType.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.RecipeTable.COL_NAME_PREPARATION_TIME_TYPE.getName())));
        } catch (IllegalArgumentException e) {
            preparationTimeType = null;
        }

        String directions = cursor.getString(cursor.getColumnIndex(ChefBuddyContract.RecipeTable.COL_NAME_DIRECTIONS.getName()));
        byte[] featuredImage = cursor.getBlob(cursor.getColumnIndex(ChefBuddyContract.RecipeTable.COL_NAME_FEATURED_IMAGE.getName()));

        return new Recipe(id, name, servings, preparationTime, preparationTimeType, directions, featuredImage);
    }


    private Ingredient getIngredientFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.RecipeIngredientTable._ID));
        String amount = cursor.getString(cursor.getColumnIndex(ChefBuddyContract.RecipeIngredientTable.COL_NAME_AMOUNT.getName()));

        Measurement measurement;
        try {
            measurement = Measurement.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.RecipeIngredientTable.COL_NAME_MEASUREMENT.getName())));
        } catch (IllegalArgumentException e) {
            measurement = Measurement.NONE;
        }

        String name = cursor.getString(cursor.getColumnIndex(ChefBuddyContract.IngredientTable.COL_NAME_NAME.getName()));

        return new Ingredient(id, amount, measurement, name);
    }



//
//
//
//
//
//
//
//    /* Cursor to Model */
//
//    private Task getTaskFromCursor(Cursor cursor) {
//        int id = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.TaskTable._ID));
//        TaskStatus status = TaskStatus.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.TaskTable.COLUMN_NAME_STATUS.getName())));
//        String title = cursor.getString(cursor.getColumnIndex(ChefBuddyContract.TaskTable.COLUMN_NAME_TITLE.getName()));
//        String description = cursor.getString(cursor.getColumnIndex(ChefBuddyContract.TaskTable.COLUMN_NAME_DESCRIPTION.getName()));
//        TaskCategory category = TaskCategory.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.TaskTable.COLUMN_NAME_CATEGORY.getName())));
//
//
//        ReminderType reminderType;
//        try {
//            reminderType = ReminderType.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.TaskTable.COLUMN_NAME_REMINDER_TYPE.getName())));
//        } catch (IllegalArgumentException e) { //Thrown if task has no reminder
//            reminderType = null;
//        }
//
//        Calendar doneDate = null;
//        if (status == TaskStatus.DONE) {
//            long doneDateLong = cursor.getLong(cursor.getColumnIndex(ChefBuddyContract.TaskTable.COLUMN_NAME_DONE_DATE.getName()));
//            if (doneDateLong != -1) {
//                doneDate = Calendar.getInstance();
//                doneDate.setTimeInMillis(doneDateLong);
//            }
//        }
//
//        return new Task(id, status, title, description, category, reminderType, null, doneDate);
//    }
//
//
//    private OneTimeReminder getOneTimeReminderFromCursor(Cursor cursor) {
//        int id = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.OneTimeReminderTable._ID));
//        int taskId = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.OneTimeReminderTable.COLUMN_NAME_TASK_FK.getName()));
//
//        Calendar date = Calendar.getInstance();
//        date.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(ChefBuddyContract.OneTimeReminderTable.COLUMN_NAME_DATE.getName())));
//
//        Time time = new Time(cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.OneTimeReminderTable.COLUMN_NAME_TIME.getName())));
//        time.setDisplayTimeFormat(SharedPreferenceUtil.getTimeFormat(mContext));
//
//        return new OneTimeReminder(id, taskId, date, time);
//    }
//
//
//    private RepeatingReminder getRepeatingReminderFromCursor(Cursor cursor) {
//        int id = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.RepeatingReminderTable._ID));
//        int taskId = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_TASK_FK.getName()));
//
//        Calendar date = Calendar.getInstance();
//        date.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_DATE.getName())));
//
//        Time time = new Time(cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_TIME.getName())));
//        time.setDisplayTimeFormat(SharedPreferenceUtil.getTimeFormat(mContext));
//
//        ReminderRepeatType repeatType = ReminderRepeatType.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_REPEAT_TYPE.getName())));
//
//        int repeatInterval = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_REPEAT_INTERVAL.getName()));
//        ReminderRepeatEndType repeatEndType = ReminderRepeatEndType.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_REPEAT_END_TYPE.getName())));
//
//        int repeatEndNumberOfEvents = -1;
//        if(repeatEndType == ReminderRepeatEndType.FOR_X_EVENTS)
//            repeatEndNumberOfEvents = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_REPEAT_END_NUMBER_OF_EVENTS.getName()));
//
//        Calendar repeatEndDate = null;
//        if(repeatEndType == ReminderRepeatEndType.UNTIL_DATE) {
//            repeatEndDate = Calendar.getInstance();
//            repeatEndDate.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(ChefBuddyContract.RepeatingReminderTable.COLUMN_NAME_REPEAT_END_DATE.getName())));
//        }
//
//        return new RepeatingReminder(id, taskId, date, time, repeatType, repeatInterval, repeatEndType, repeatEndNumberOfEvents, repeatEndDate);
//    }
//
//
//    private LocationBasedReminder getLocationBasedReminderFromCursor(Cursor cursor) {
//        int id = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.LocationBasedReminderTable._ID));
//        int taskId = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TASK_FK.getName()));
//        int placeId = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_PLACE_FK.getName()));
//        boolean triggerEntering = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TRIGGER_ENTERING.getName())));
//        boolean triggerExiting = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.LocationBasedReminderTable.COLUMN_NAME_TRIGGER_EXITING.getName())));
//
//        return new LocationBasedReminder(id, taskId, placeId, null, triggerEntering, triggerExiting);
//    }
//
//
//    private Place getPlaceFromCursor(Cursor cursor) {
//        int id = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.PlaceTable._ID));
//        String alias = cursor.getString(cursor.getColumnIndex(ChefBuddyContract.PlaceTable.COLUMN_NAME_ALIAS.getName()));
//        String address = cursor.getString(cursor.getColumnIndex(ChefBuddyContract.PlaceTable.COLUMN_NAME_ADDRESS.getName()));
//        double latitude = cursor.getDouble(cursor.getColumnIndex(ChefBuddyContract.PlaceTable.COLUMN_NAME_LATITUDE.getName()));
//        double longitude = cursor.getDouble(cursor.getColumnIndex(ChefBuddyContract.PlaceTable.COLUMN_NAME_LONGITUDE.getName()));
//        int radius = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.PlaceTable.COLUMN_NAME_RADIUS.getName()));
//        boolean isOneOff = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.PlaceTable.COLUMN_NAME_IS_ONE_OFF.getName())));
//
//        return new Place(id, alias, address, latitude, longitude, radius, isOneOff);
//    }
//
//    private Attachment getAttachmentFromCursor(Cursor cursor) {
//        int id = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.AttachmentTable._ID));
//        int reminderId = cursor.getInt(cursor.getColumnIndex(ChefBuddyContract.AttachmentTable.COLUMN_NAME_TASK_FK.getName()));
//        AttachmentType attachmentType = AttachmentType.valueOf(cursor.getString(cursor.getColumnIndex(ChefBuddyContract.AttachmentTable.COLUMN_NAME_TYPE.getName())));
//        String textContent = cursor.getString(cursor.getColumnIndex(ChefBuddyContract.AttachmentTable.COLUMN_NAME_CONTENT_TEXT.getName()));
//        byte[] blobContent = cursor.getBlob(cursor.getColumnIndex(ChefBuddyContract.AttachmentTable.COLUMN_NAME_CONTENT_BLOB.getName()));
//
//        switch (attachmentType) {
//            case AUDIO:
//                return new AudioAttachment(id, reminderId, textContent);
//            case IMAGE:
//                return new ImageAttachment(id, reminderId, blobContent, textContent);
//            case TEXT:
//                return new TextAttachment(id, reminderId, textContent);
//            case LINK:
//                return new LinkAttachment(id, reminderId, textContent);
//            case LIST:
//                return new ListAttachment(id, reminderId, textContent);
//            default:
//                throw new InvalidParameterException("AttachmentType is invalid. Value = " + attachmentType);
//        }
//    }

}