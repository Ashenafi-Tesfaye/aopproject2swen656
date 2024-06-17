document.addEventListener('DOMContentLoaded', () => {
    const addressBook = document.getElementById('address-book');
    const addContactForm = document.getElementById('addContactForm');
    const updateContactForm = document.getElementById('updateContactForm');
    const deleteContactForm = document.getElementById('deleteContactForm');
    const addContactSection = document.getElementById('add-contact-section');
    const contactsTable = document.getElementById('contacts-table');

    function displayError(errors, errorDiv) {
        if (errorDiv) {
            errorDiv.innerHTML = `<p>Error:</p><ul>${errors.map(error => `<li>${error}</li>`).join('')}</ul>`;
        } else {
            console.error('Error div not found in the DOM');
        }
    }

    function clearForm(form) {
        form.reset();
    }

    function reloadAddressBook() {
        fetchContacts()
            .then(data => {
                renderContacts(data);
                location.reload();
            })
            .catch(error => {
                displayError([error.message], document.getElementById('addContactError'));
            });
    }

    function fetchContacts() {
        const requestOptions = {
            method: 'GET',
            headers: {
                'API_KEY': 'ag_api_key',
                'API_SECRET': 'swen656',
                'Content-Type': 'application/json'
            }
        };

        return fetch('/api/contacts', requestOptions)
            .then(response => {
                if (!response.ok) {
                    console.error("Failed to fetch contacts: ", response.statusText); 
                    throw new Error(`Failed to fetch contacts: ${response.statusText}`);
                }
                return response.json();
            }).catch(error => {
                console.error("Failed to fetch contacts: ", error);
                throw new Error(`Failed to fetch contacts: ${error}`);
            });
    }

    function renderContacts(contacts) {
        const contactsList = document.getElementById('contacts-list');
        if (contactsList) {
            contactsList.innerHTML = '';
            contactsTable.innerHTML = ''; // Clear existing table content
            contacts.forEach(contact => {
                const row = contactsTable.insertRow();
                row.innerHTML = `
                    <td>${contact.id}</td>
                    <td>${contact.name}</td>
                    <td>${contact.street}</td>
                    <td>${contact.city}</td>
                    <td>${contact.state}</td>
                    <td>${contact.zip}</td>
                    <td>${contact.phone}</td>
                    <td>
                        <button class="btn-update" data-id="${contact.id}">Update</button>
                        <button class="btn-delete" data-id="${contact.id}">Delete</button>
                    </td>
                `;
            });
        } else {
            console.error('Contacts list or table not found in the DOM');
        }
    }

    function addContact(formData) {
        const requestOptions = {
            method: 'POST',
            headers: {
                'API_KEY': 'ag_api_key',
                'API_SECRET': 'swen656',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: formData.get('name'),
                street: formData.get('street'),
                city: formData.get('city'),
                state: formData.get('state'),
                zip: formData.get('zip'),
                phone: formData.get('phone')
            })
        };

        return fetch('/api/contacts', requestOptions)
            .then(response => {
                if (!response.ok) {
                    throw new Error(response.statusText);
                }
                return response.json();
            });
    }

    function updateContact(formData) {
        const id = formData.get('updateId');
        const requestOptions = {
            method: 'PUT',
            headers: {
                'API_KEY': 'ag_api_key',
                'API_SECRET': 'swen656',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: formData.get('updateName'),
                street: formData.get('updateStreet'),
                city: formData.get('updateCity'),
                state: formData.get('updateState'),
                zip: formData.get('updateZip'),
                phone: formData.get('updatePhone')
            })
        };

        return fetch(`/api/contacts/${id}`, requestOptions)
            .then(response => {
                if (!response.ok) {
                    throw new Error(response.statusText);
                }
                return response.json();
            });
    }

    function deleteContact(id) {
        const requestOptions = {
            method: 'DELETE',
            headers: {
                'API_KEY': 'ag_api_key',
                'API_SECRET': 'swen656'
            }
        };

        return fetch(`/api/contacts/${id}`, requestOptions)
            .then(response => {
                if (!response.ok) {
                    throw new Error(response.statusText);
                }
            });
    }

    function fetchContactById(id) {
        const requestOptions = {
            method: 'GET',
            headers: {
                'API_KEY': 'ag_api_key',
                'API_SECRET': 'swen656',
                'Content-Type': 'application/json'
            }
        };

        return fetch(`/api/contacts/${id}`, requestOptions)
            .then(response => {
                if (!response.ok) {
                    throw new Error(response.statusText);
                }
                return response.json();
            });
    }

    function populateUpdateForm(contact) {
        updateContactForm.updateId.value = contact.id;
        updateContactForm.updateName.value = contact.name;
        updateContactForm.updateStreet.value = contact.street;
        updateContactForm.updateCity.value = contact.city;
        updateContactForm.updateState.value = contact.state;
        updateContactForm.updateZip.value = contact.zip;
        updateContactForm.updatePhone.value = contact.phone;
    }

    // Initial fetch and render
    fetchContacts()
        .then(data => renderContacts(data))
        .catch(error => displayError([error.message], document.getElementById('addContactError')));

    // Event listeners
    addressBook.addEventListener('click', event => {
        if (event.target.classList.contains('btn-update')) {
            const id = event.target.getAttribute('data-id');
            fetchContactById(id)
                .then(contact => {
                    populateUpdateForm(contact);
                    // Show update form, hide add contact form
                    addContactSection.classList.add('hidden');
                    document.getElementById('update-contact').classList.remove('hidden');
                })
                .catch(error => {
                    console.error('Error fetching contact details:', error);
                    displayError([error.message], document.getElementById('updateContactError'));
                });
        } else if (event.target.classList.contains('btn-delete')) {
            const id = event.target.getAttribute('data-id');
            deleteContact(id)
                .then(() => reloadAddressBook())
                .catch(error => displayError([error.message], document.getElementById('deleteContactError')));
        }
    });

    updateContactForm.addEventListener('submit', event => {
        event.preventDefault();
        const formData = new FormData(updateContactForm);
        updateContact(formData)
            .then(() => {
                clearForm(updateContactForm);
                reloadAddressBook();
                // Hide update form after submission
                updateContactForm.classList.add('hidden');
            })
            .catch(error => displayError([error.message], document.getElementById('updateContactError')));
    });

    deleteContactForm.addEventListener('submit', event => {
        event.preventDefault();
        const id = deleteContactForm.deleteId.value;
        deleteContact(id)
            .then(() => {
                clearForm(deleteContactForm);
                reloadAddressBook();
                // Hide delete form after submission
                deleteContactForm.classList.add('hidden');
            })
            .catch(error => displayError([error.message], document.getElementById('deleteContactError')));
    });

    addContactForm.addEventListener('submit', event => {
        event.preventDefault();
        const formData = new FormData(addContactForm);
        addContact(formData)
            .then(() => {
                clearForm(addContactForm);
                reloadAddressBook();
            })
            .catch(error => displayError([error.message], document.getElementById('addContactError')));
    });
});
