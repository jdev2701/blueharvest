package hussam.blueharvest.account.exception;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import hussam.blueharvest.account.constant.ErrorMessage;
import hussam.blueharvest.account.model.ErrorResponse;

@ControllerAdvice
public class DefaultExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	@Autowired
	private MessageSource messages;

	/**
	 * Handler for NotFound exception.
	 *
	 * @param e
	 *            NotFoundException
	 * @return ErrorResponse
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	public ErrorResponse handleNotFoundException(NotFoundException e) {
		log.error(e.getMessage(), e);

		return getLocalizedMessage(e.getErrorMessage(), Collections.singletonList(e.getMessage()), Locale.ENGLISH);
	}

	/**
	 * Handler for ServiceDownException exception.
	 *
	 * @param e
	 *            ServiceDownException
	 * @return ErrorResponse
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ServiceDownException.class)
	@ResponseBody
	public ErrorResponse handleServiceDownException(ServiceDownException e) {
		log.error(e.getMessage(), e);

		return getLocalizedMessage(e.getErrorMessage(), Collections.singletonList(e.getMessage()), Locale.ENGLISH);
	}

	/**
	 * Handles validation and missing required parameter errors on requests
	 *
	 * @param e
	 *            Exception
	 * @return ErrorResponse
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class })
	@ResponseBody
	public ErrorResponse handleRequestParameterExceptions(Exception e) {
		log.error(e.getMessage(), e);

		return getLocalizedMessage(ErrorMessage.BAD_REQUEST, Collections.singletonList(e.getMessage()), Locale.ENGLISH);
	}

	/**
	 * Handler for for any un-handled exception.
	 * 
	 * @param e
	 *            Exception
	 * @return ErrorResponse
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ErrorResponse handleUnhandledException(Exception e) {

		log.error("unhandledException:", e);

		Locale locale = Locale.ENGLISH;

		return getLocalizedMessage(ErrorMessage.INTERNAL_ERROR, Collections.singletonList(e.getMessage()), locale);
	}

	private ErrorResponse getLocalizedMessage(ErrorMessage code, List<String> parameters, final Locale locale) {

		String localMessage = this.messages.getMessage(code.name(), new String[0], null, locale);

		if (StringUtils.isEmpty(localMessage) && parameters != null && !parameters.isEmpty()) {
			localMessage = parameters.get(0);
		}

		return new ErrorResponse(code.name(), localMessage);

	}
}
