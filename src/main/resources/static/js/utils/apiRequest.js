export async function apiRequest(endpoint, method = 'GET', body = null) {
    const headers = { 'Content-Type': 'application/json' };

    const options = {
        method,
        headers,
        ...(body && { body: JSON.stringify(body) }),
    };

    try {
        const response = await fetch(`/api/${endpoint}`, options);

        if (!response.ok) {
            const errorBody = await response.json().catch(() => ({}));
            throw new Error(errorBody.message || 'Unexpected error occurred');
        }

        const contentType = response.headers.get('content-type');
        if (response.status === 204 || !contentType || !contentType.includes('application/json')) {
            return null;
        }

        return await response.json();
    } catch (error) {
        throw new Error(error.message || 'Network error');
    }
}