package com.globant.project.services;

import java.io.IOException;

/**
 * MenuService
 */
public interface MenuService {

    String getMenuPlainText();

    byte[] getMenuPdf() throws IOException;

}
