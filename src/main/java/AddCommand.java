import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Class representing commands to add task to TaskList
 */
public class AddCommand extends Command {

    // Attributes
    private final String type;
    private final String description;

    // Constructor

    /**
     * Creates a new command to add task to the list
     * @param type Type of task to be added to the list.
     * @param description Description of task.
     */
    public AddCommand(String type, String description) {
        this.type = type;
        this.description = description;
    }

    // Methods

    /**
     * Executes the command to add a task by adding task into given TaskList.
     *
     * @param tasks   TaskList representing list of current tasks.
     * @param ui      Ui object to handle printing of outputs.
     * @param storage Storage object to handle saving of outputs to computer
     * @throws EmptyBodyException    If command is to add event or deadline but no date and time is provided.
     * @throws UnknownInputException If an unrecognised command is given.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage, Map<String, Runnable> runnables)
            throws EmptyBodyException, UnknownInputException {
        switch (this.type) {
        case "todo": {
            Task newTodo = new Todo(description);
            return tasks.createTask(newTodo);
        }
        case "deadline": {
            String[] text = description.split(" /by ");
            String description = text[0];
            if (text.length <= 1) {
                throw new EmptyBodyException("deadline", "deadline");
            }
            try {
                LocalDate deadline = LocalDate.parse(text[1]);
                Task newDeadline = new Deadline(description, deadline);
                return tasks.createTask(newDeadline);
            } catch (DateTimeParseException e) {
                throw new UnknownInputException(text[1]);
            }
        }
        case "event": {
            String[] text = description.split(" /at ");
            String description = text[0];
            if (text.length <= 1) {
                throw new EmptyBodyException("date and time", "event");
            }
            String dateTime = text[1];
            Task newEvent = new Event(description, dateTime);
            return tasks.createTask(newEvent);
        }
        default:
            throw new UnknownInputException(this.type);
        }
    }

    /**
     * Returns whether the command is a command to exit.
     *
     * @return false.
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
