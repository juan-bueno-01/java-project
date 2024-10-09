package com.globant.project.application.ports.in.services;

import java.io.IOException;

/**
 * MenuService
 */
public interface MenuService {

    String getMenuPlainText();

    byte[] getMenuPdf() throws IOException;

}
