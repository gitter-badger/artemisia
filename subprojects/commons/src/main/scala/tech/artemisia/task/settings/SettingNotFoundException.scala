package tech.artemisia.task.settings

/**
 * Created by chlr on 4/26/16.
 */

/**
 * Throw this exception when mandatory setting keys are missing
 * @param message
 */
class SettingNotFoundException(message: String) extends Exception(message)
