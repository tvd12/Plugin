package com.tvd.gamview.ext.pages;

import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.tvd.gameview.plugin.model.DirectorySelectionField;
import com.tvd.gameview.plugin.model.FileSelectionField;
import com.tvd.gameview.plugin.model.ProjectChooserHelper;
import com.tvd.gamview.ext.utils.MessageUtils;

public class NewGameViewFilePage extends WizardPage {

	public NewGameViewFilePage(String pageName,
			IStructuredSelection selection) {
		super(pageName);
		this.setPageComplete(false);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setFont(parent.getFont());
		
		this.initializeDialogUnits(parent);
		
		composite.setLayout(new GridLayout(3, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        this.createDefineGroup(composite);
        this.createSeparator(composite);
        
        this.createImageGroup(composite);
        this.createSeparator(composite);
        
        this.createScreenContainerGroup(composite);
        this.createSeparator(composite);
        
        this.createClassContainerGroup(composite);
        this.createSeparator(composite);
        
        this.createProjectsGroup(composite);
        
		this.setErrorMessage(null);
		this.setMessage(null);
		this.setControl(composite);
		
		this.setPageComplete(false);
	}
	
	
	protected boolean validatePage() {
		boolean valid = true;
		String error = null;
		String warning = null;
		if(!validateFieldValue(mDefineSelectionField.getText().getText(), ".h")) {
			valid = false;
			error = "Invalid define path";
		} else if(!validateFieldValue(mParamsSelectionField.getText().getText(), ".h")) {
			valid = false;
			error = "Invalid parameter path";
		} else if(!validateFieldValue(mImageInputPathField.getText().getText(), "")) {
			valid = false;
			error = "Invalid image input path";
		} else if(!validateFieldValue(mImageOutputPathField.getText().getText(), "")) {
			valid = false;
			error = "Invalid image output path";
		} else if(!validateFieldValue(mXibContainerPathField.getText().getText(), "")) {
			valid = false;
			error = "Invalid xib container path";
		} else if(!validateFieldValue(mScreenContainerPathField.getText().getText(), "")) {
			valid = false;
			error = "Invalid screen container path";
		} else if(mProjectCombo.getSelectionIndex() == 0) {
			valid = false;
			error = "Please select a project";
		} else if(mFileNameTextField.getText() == null 
				&& mFileNameTextField.getText().equals("")) {
			valid = false;
			error = "Invalid file name";
		} else if(!validateFieldValue(mClassPathField.getText().getText(), "")) {
			valid = false;
			error = "Invalid class path";
		}
		if(valid) {
			if(mClassPathField.getText().getText()
					.trim().equals(mGameProjectPath + "/Classes")) {
				warning = "You are using default class path";
			}
			if(mImageOutputPathField.getText().getText()
					.trim().equals(mGameProjectPath + "/Resources")) {
				warning = "You are using default resources path";
			}
			this.setMessage(warning, WARNING);
		}
		this.setErrorMessage(error);
		this.setPageComplete(valid);
		return valid;
	}
	private boolean validateFieldValue(String fieldValue, String extension) {
		boolean valid = true; 
		if(fieldValue == null || fieldValue.equals("")) {
			valid = false;
		} else {
			File file = new File(fieldValue + extension);
			valid = file.exists();
		}
		
		return valid;
	}
	
	private void createDefineGroup(final Composite parent) {
		this.mDefineSelectionField = new FileSelectionField.Builder(parent)
				.setLabelText("Define path:")
				.build();
		this.mParamsSelectionField = new FileSelectionField.Builder(parent)
			.setLabelText("Parameters path:")
			.build();
		this.mDefineSelectionField.getText().addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				updateText((Text)e.getSource());
				validatePage();
			}
		});
		this.mParamsSelectionField.getText().addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				updateText((Text)e.getSource());
				validatePage();
			}
		});
	}
	
	private void createImageGroup(final Composite parent) {
		this.mImageInputPathField = new DirectorySelectionField.Builder(parent)
			.setLabelText("Images input path:")
			.build();
		this.mImageOutputPathField = new DirectorySelectionField.Builder(parent)
			.setLabelText("Images output path:")
			.build();
		this.mImageInputPathField.getText().addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				String text = ((Text)e.getSource()).getText();
				mFileNameTextField.setText(text.substring(text.lastIndexOf('/') + 1));
			}
		});
		this.addModifyListenerForField(mImageOutputPathField.getText());
	}
	
	private void createScreenContainerGroup(final Composite parent) {
		this.mXibContainerPathField = new DirectorySelectionField.Builder(parent)
			.setLabelText("Xib container path: ")
			.build();
		this.mScreenContainerPathField = new DirectorySelectionField.Builder(parent)
			.setLabelText("Screen container path: ")
			.build();
		this.addModifyListenerForField(mXibContainerPathField.getText());
		this.addModifyListenerForField(mScreenContainerPathField.getText());
	}
	
	private void createClassContainerGroup(final Composite parent) {
		this.mClassPathField = new DirectorySelectionField.Builder(parent)
			.setLabelText("Class path")
			.build();
		this.addModifyListenerForField(mClassPathField.getText());
	}
	
	private void createProjectsGroup(Composite parent) {
        String tooltip = "The Android Project where the new resource file will be created.";
        Label label = new Label(parent, SWT.NONE);
        label.setText("Project: ");
        label.setToolTipText(tooltip);
        
        mProjectCombo = new GameViewProjectCombo(parent);
        
        emptyCell(parent);
        
        createFileNameField(parent);
        
    }
	
	private void createFileNameField(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
        label.setText("File");
        label.setToolTipText("The name of view");
        
        mFileNameTextField = new Text(parent, SWT.BORDER);
        mFileNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mFileNameTextField.setToolTipText("The name of the resource file to create.");
        mFileNameTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validatePage();
            }
        });
        
        emptyCell(parent);
        
    }
	
	
	private void createSeparator(Composite composite) {
		Label rootSeparator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd = new GridData(GridData.GRAB_HORIZONTAL);
        gd.horizontalAlignment = SWT.FILL;
        gd.horizontalSpan = 3;
        rootSeparator.setLayoutData(gd);
	}
	
	private void addModifyListenerForField(Text text) {
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				validatePage();
				
			}
		});
	}
	
    private void emptyCell(Composite parent) {
        new Label(parent, SWT.NONE);
    }
    
    private void updateText(Text text) {
		String str = text.getText();
		if(str == null || str.equals("")) {
			return;
		}
		String fileName = str.substring(str.lastIndexOf('/') + 1);
		if(fileName.contains(".")) {
			str = str.substring(0, str.lastIndexOf('.'));
			text.setText(str);
		}
    }
    
    private void setTextForTextFieldWithProject(IProject project) {
    	if(project != null) {
			try {
				InputStream inputStream = 
						project.getFile("src/com/properties/global.properties")
						.getContents();
				String gameProject = MessageUtils.getString(inputStream, "game_project");
				mDefineSelectionField.getText().setText(gameProject
						+ "/Classes/Utils/Defines");
				mParamsSelectionField.getText().setText(gameProject 
						+ "/Classes/Utils/GameParams");
				mXibContainerPathField.getText().setText(gameProject
						+ "/Resources/screenshots");
				mScreenContainerPathField.getText().setText(gameProject
						+ "/Wireframing/WireframingScreen");
				mClassPathField.getText().setText(gameProject
						+ "/Classes");
				mImageOutputPathField.getText().setText(gameProject
						+ "/Resources");
				mGameProjectPath = gameProject;
			} catch (CoreException ex) {
				ex.printStackTrace();
			}
		}
    }
	
    private class GameViewProjectCombo extends ProjectChooserHelper.ProjectCombo {

		public GameViewProjectCombo(Composite parent) {
			super(parent);
			setTextForTextFieldWithProject(mInitialProject);
		}
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			super.widgetSelected(e);
			setTextForTextFieldWithProject(mProject);
			validatePage();
		}
    	
    }
    
	private DirectorySelectionField mClassPathField;
	
	private DirectorySelectionField mXibContainerPathField;
	private DirectorySelectionField mScreenContainerPathField;
	
	private DirectorySelectionField mImageInputPathField;
	private DirectorySelectionField mImageOutputPathField;
	
	private FileSelectionField mDefineSelectionField;
	private FileSelectionField mParamsSelectionField;
	

	public DirectorySelectionField getClassPathField() {
		return mClassPathField;
	}

	public DirectorySelectionField getXibContainerPathField() {
		return mXibContainerPathField;
	}

	public DirectorySelectionField getScreenContainerPathField() {
		return mScreenContainerPathField;
	}

	public DirectorySelectionField getImageInputPathField() {
		return mImageInputPathField;
	}

	public DirectorySelectionField getImageOutputPathField() {
		return mImageOutputPathField;
	}

	public FileSelectionField getDefineSelectionField() {
		return mDefineSelectionField;
	}

	public FileSelectionField getParamsSelectionField() {
		return mParamsSelectionField;
	}

	public Combo getProjectCombo() {
		return mProjectCombo;
	}

	public Text getFileNameTextField() {
		return mFileNameTextField;
	}

	private Combo mProjectCombo;
	private Text mFileNameTextField;
	private String mGameProjectPath;
}

