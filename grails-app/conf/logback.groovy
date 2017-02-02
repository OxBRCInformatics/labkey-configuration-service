import ch.qos.logback.classic.filter.ThresholdFilter
import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

File baseDir = BuildSettings.BASE_DIR.canonicalFile
File targetDir = BuildSettings.TARGET_DIR.canonicalFile

def logDir = new File(targetDir, 'logs')
if (!logDir) logDir.mkdirs()

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')

        pattern =
                '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                '%clr(%5p) ' + // Log level
                '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                '%m%n%wex' // Message
    }
    if (Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) {
        filter(ThresholdFilter) {
            level = WARN
        }
    }
    else {
        filter(ThresholdFilter) {
            level = ERROR
        }
    }
}


appender("FILE", FileAppender) {
    file = "${logDir}/${baseDir.name.toLowerCase()}.log"
    append = false
    encoder(PatternLayoutEncoder) {
        pattern = '%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n'
    }
}

root(INFO, ['STDOUT', 'FILE'])

if (Environment.current == Environment.DEVELOPMENT || Environment.current == Environment.TEST) {

    // logger('org.grails.orm.hibernate.cfg.HibernateMappingBuilder', OFF)
    logger('org.hibernate.tool.hbm2ddl.SchemaExport', OFF)
    logger('uk.ac.ox', DEBUG)
    //logger('org.hibernate.SQL', DEBUG)
    //  logger 'org.hibernate.type', TRACE
    logger('org.grails.spring.beans.factory.OptimizedAutowireCapableBeanFactory', ERROR)
    logger('org.springframework.context.support.PostProcessorRegistrationDelegate', WARN)

}